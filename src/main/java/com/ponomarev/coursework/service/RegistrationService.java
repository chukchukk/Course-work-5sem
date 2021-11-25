package com.ponomarev.coursework.service;

import com.ponomarev.coursework.dto.UserDTO;
import com.ponomarev.coursework.model.CardInfo;
import com.ponomarev.coursework.model.ConfirmationToken;
import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.model.UserInfo;
import com.ponomarev.coursework.repository.CardInfoRepository;
import com.ponomarev.coursework.repository.UserInfoRepository;
import com.ponomarev.coursework.repository.UserRepository;
import com.ponomarev.coursework.repository.redis.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

//TODO проверка на последовательность шагов в каждом методе
@Service
@AllArgsConstructor
public class RegistrationService implements BaseService{

	private final CardInfoRepository cardInfoRepository;
	private final MailSenderService mailSenderService;
	private final ConfirmationTokenRepository confirmationTokenRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final UserInfoRepository userInfoRepository;

	public String getFirstStepPage(HttpServletRequest request, Model model) {
		requestModelFilling(request, model);
		return "registration/registration_page1";
	}

	public String checkCardAndSend(String cardNumber, RedirectAttributes redirectAttributes) {
		Optional<CardInfo> infoByCardNumber = cardInfoRepository.findCardInfoByCardNumber(cardNumber);

		if (!infoByCardNumber.isPresent()) {
			redirectAttributes.addFlashAttribute("cardNumberErr", "Check your card number");
			return "redirect:/registration/firstStep";
		}

		UserInfo userInfo = infoByCardNumber.get().getUserInfo();
		if (userInfo.getUser() != null) {
			redirectAttributes.addFlashAttribute("cardNumberErr", "Account is already exists");
			return "redirect:/registration/firstStep";
		}
		if (confirmationTokenRepository.findById(userInfo.getId().toString()).isPresent()) {
			redirectAttributes.addFlashAttribute("flag", false);
			redirectAttributes.addFlashAttribute("mailTo", "Confirmation token has already been sent to " + userInfo.getEmail());
			redirectAttributes.addFlashAttribute("userInfoID", userInfo.getId());
			return "redirect:/registration/completion";
		}

		mailSenderService.sendForRegistration(userInfo);
		redirectAttributes.addFlashAttribute("flag", false);
		redirectAttributes.addFlashAttribute("mailTo", userInfo.getEmail());
		redirectAttributes.addFlashAttribute("userInfoID", userInfo.getId());
		return "redirect:/registration/completion";
	}

	public String getSecondStepPage(HttpServletRequest request, Model model) {
		requestModelFilling(request, model);
		return "registration/registration_page2";
	}

	public String checkConfirmationCode(Long id, String confirmationCode, RedirectAttributes redirectAttributes) {
		Optional<ConfirmationToken> redisTokenForCheck = confirmationTokenRepository.findById(id.toString());
		if (redisTokenForCheck.isPresent()) {
			ConfirmationToken redisToken = redisTokenForCheck.get();
			if (redisToken.getConfirmationToken().equals(confirmationCode)) {
				redirectAttributes.addFlashAttribute("userInfoID", id);
				redirectAttributes.addFlashAttribute("flag", true);
				return "redirect:/registration/completion";
			}
		}
		//TODO
		redirectAttributes.addFlashAttribute("flag", false);
		redirectAttributes.addFlashAttribute("confirmationCodeErr", "ОШИБКА");
		return "redirect:/registration/completion";
	}

	public String createUser(UserDTO userDTO, Long id, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("flag", true);
			fillErrors(bindingResult, redirectAttributes);
			return "redirect:/registration/completion";
		}
		//TODO проверка на логин в бд
		UserInfo userInfo = userInfoRepository.findById(id).get();
		User user = new User();
		user.setLogin(userDTO.getLogin());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setInformation(userInfo);
		user.setActive(true);
		user.setRoles(Collections.singleton(User.Role.USER));

		userInfo.setUser(user);
		userInfoRepository.save(userInfo);
		return "redirect:/login";
	}
}
