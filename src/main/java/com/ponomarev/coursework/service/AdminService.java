package com.ponomarev.coursework.service;

import com.ponomarev.coursework.dto.CardInfoDTO;
import com.ponomarev.coursework.dto.RegisterNewClientDTO;
import com.ponomarev.coursework.model.CardInfo;
import com.ponomarev.coursework.model.PassportInfo;
import com.ponomarev.coursework.model.UserInfo;
import com.ponomarev.coursework.repository.CardInfoRepository;
import com.ponomarev.coursework.repository.PassportInfoRepository;
import com.ponomarev.coursework.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AdminService implements BaseService{

    private final UserInfoRepository userInfoRepository;

    private final PassportInfoRepository passportInfoRepository;

    private final CardInfoRepository cardInfoRepository;

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

    public String createCardForUser(Long id, CardInfoDTO cardInfoDTO, BindingResult errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            fillErrors(errors, redirectAttributes);
            return "redirect:/admin/createCardForUser/" + id;
        }
        Optional<UserInfo> userById = userInfoRepository.findById(id);
        if (userById.isPresent()) {
            UserInfo userInfo = userById.get();
            Set<CardInfo> usersCard = userInfo.getCardInfo();
            CardInfo newCard = new CardInfo();
            newCard.setCardNumber(cardInfoDTO.getCardNumber());
            newCard.setValidTHRU(cardInfoDTO.getValidTHRU());
            newCard.setCvv(cardInfoDTO.getCvv());
            newCard.setActive(true);
            newCard.setUserInfo(userInfo);
            newCard.setBalance((double) 0);

            if (userHasCardWithNumber(usersCard, cardInfoDTO.getCardNumber())) {
                redirectAttributes.addFlashAttribute("cardNumberExist", "Card with number already exists");
                return "redirect:/admin/createCardForUser/" + id;
            }

            cardInfoRepository.save(newCard);
        } else {
            redirectAttributes.addFlashAttribute("clientNotFound", "Client not found");
        }
        redirectAttributes.addFlashAttribute("success", "Successfully");
        return "redirect:/admin/createCardForUser/" + id;
    }

    private boolean userHasCardWithNumber(Set<CardInfo> usersCard, String cardNumber) {
        return usersCard.stream().anyMatch(cardInfo -> cardInfo.getCardNumber().equals(cardNumber));
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

}
