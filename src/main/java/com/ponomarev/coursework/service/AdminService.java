package com.ponomarev.coursework.service;

import com.ponomarev.coursework.dto.CardInfoDTO;
import com.ponomarev.coursework.dto.RegisterNewClientDTO;
import com.ponomarev.coursework.model.PassportInfo;
import com.ponomarev.coursework.model.UserInfo;
import com.ponomarev.coursework.repository.PassportInfoRepository;
import com.ponomarev.coursework.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserInfoRepository userInfoRepository;

    private final PassportInfoRepository passportInfoRepository;

    public String newCardPage(HttpServletRequest request, Model model) {
        requestModelFilling(request, model);
        return "admin/register_new_client";
    }

    public String clientListPage(HttpServletRequest request, Model model) {
        requestModelFilling(request, model);
        List<UserInfo> users = userInfoRepository.findAll();
        if (!users.isEmpty()) {
            model.addAttribute("clients", users);
        } else {
            model.addAttribute("emptyList", "Empty list of clients");
        }
        return "admin/client_list";
    }

    private void requestModelFilling(HttpServletRequest request, Model model) {
        Map<String, ?> flashAttributes = RequestContextUtils.getInputFlashMap(request);

        if(flashAttributes != null) {
            for (Map.Entry<String, ?> keyVal : flashAttributes.entrySet()) {
                model.addAttribute(keyVal.getKey(), keyVal.getValue());
            }
        }
    }

    @SneakyThrows
    public String registerNewClient(BindingResult errors,
                                    RegisterNewClientDTO dto, RedirectAttributes redirectAttributes) {
        if(errors.hasErrors()) {
            fillErrors(errors, redirectAttributes);
            redirectAttributes.addFlashAttribute("lastName",
                    dto.getLastName());
            redirectAttributes.addFlashAttribute("firstName",
                    dto.getFirstName());
            redirectAttributes.addFlashAttribute("middleName",
                    dto.getMiddleName());
            redirectAttributes.addFlashAttribute("email",
                    dto.getEmail());
            redirectAttributes.addFlashAttribute("passportSeries",
                    dto.getPassportSeries());
            redirectAttributes.addFlashAttribute("passportNumber",
                    dto.getPassportNumber());
            redirectAttributes.addFlashAttribute("failed", "Not successful");
            return "redirect:/admin/newCard";
            }
        Optional<PassportInfo> passportInfoFromBD = passportInfoRepository.findAllByPassportSeriesAndPassportNumber(dto.getPassportSeries(),
                dto.getPassportNumber());
        if (passportInfoFromBD.isPresent()) {
            redirectAttributes.addFlashAttribute("failed", "Client with passport data already exists");
            return "redirect:/admin/newCard";
        }
        if (!isMoreThan14(dto.getBirthdayDate())) {
            redirectAttributes.addFlashAttribute("failed", "The client is less than 14 years old");
            return "redirect:/admin/newCard";
        }

        UserInfo userInfoFromDTO = this.createUserInfoFromDTO(dto);
        PassportInfo passportInfoFromDTO = this.createPassportInfoFromDTO(dto);
        userInfoFromDTO.setPassportInfo(passportInfoFromDTO);
        userInfoRepository.save(userInfoFromDTO);

        redirectAttributes.addFlashAttribute("success", "The client is successfully registered");
        return "redirect:/admin/newCard";
    }

    public String createCardForUserPage(HttpServletRequest request, Model model, Long id) {
        requestModelFilling(request, model);
        Optional<UserInfo> userInfo = userInfoRepository.findById(id);
        model.addAttribute("client", userInfo.get());
        return "admin/create_card";
    }
    //TODO доделать заведение новой карты для пользователя
    public String createCardForUser(Long id, CardInfoDTO cardInfoDTO, BindingResult errors, RedirectAttributes redirectAttributes) {
        if()
        fillErrors(errors, redirectAttributes);
    }

    private boolean isMoreThan14(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parse = LocalDate.parse(date, formatter);
        LocalDate now = LocalDate.now();
        return Period.between(parse, now).getYears() > 14;
    }
    private UserInfo createUserInfoFromDTO(RegisterNewClientDTO dto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName(dto.getFirstName());
        userInfo.setLastName(dto.getLastName());
        userInfo.setMiddleName(dto.getMiddleName());
        userInfo.setEmail(dto.getEmail());
        userInfo.setBirthdayDate(dto.getBirthdayDate());
        return userInfo;
    }

    private PassportInfo createPassportInfoFromDTO(RegisterNewClientDTO dto) {
        PassportInfo passportInfo = new PassportInfo();
        passportInfo.setPassportSeries(dto.getPassportSeries());
        passportInfo.setPassportNumber(dto.getPassportNumber());
        passportInfo.setIssueDate(dto.getPassportIssueDate());
        return passportInfo;
    }

    private void fillErrors(BindingResult errors, RedirectAttributes redirectAttributes) {
        List<FieldError> listOfErrors = errors.getFieldErrors();
        for (FieldError f : listOfErrors) {
            redirectAttributes.addFlashAttribute(f.getField() + "Err",
                    f.getDefaultMessage());
        }
    }

}
