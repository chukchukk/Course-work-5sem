package com.ponomarev.coursework.service;

import com.ponomarev.coursework.dto.ChangeLoginEmailDTO;
import com.ponomarev.coursework.dto.ChangePasswordDTO;
import com.ponomarev.coursework.entity.User;
import com.ponomarev.coursework.entity.UserInfo;
import com.ponomarev.coursework.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class ClientInfoService implements BaseService {

	private final UserInfoRepository userInfoRepository;

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder encoder;

	public String clientInfoPage(User user, HttpServletRequest request, Model model) {
		requestModelFilling(request, model);
		UserInfo userInfo = userInfoRepository.findByUser(user);
		model.addAttribute("login", user.getLogin());
		model.addAttribute("email", userInfo.getEmail());
		return "client/client_info";
	}

	public String changeClientInfo(User user,
								   ChangeLoginEmailDTO dto,
								   BindingResult errors,
								   RedirectAttributes redirectAttributes) {
		if (dto.getEmail().isEmpty() && dto.getLogin().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Type new login or email");
			return "redirect:/client/changeClientInfo";
		}
		if (errors.hasErrors()) {
			fillErrors(errors, redirectAttributes);
			redirectAttributes.addFlashAttribute("currentLogin", dto.getLogin());
			redirectAttributes.addFlashAttribute("currentEmail", dto.getEmail());
			return "redirect:/client/changeClientInfo";
		}
		if (!dto.getLogin().isEmpty()) {
			user.setLogin(dto.getLogin());
			userRepository.save(user);
		}
		if (!dto.getEmail().isEmpty()) {
			UserInfo userInfo = userInfoRepository.findByUser(user);
			userInfo.setEmail(dto.getEmail());
			userInfoRepository.save(userInfo);
		}
		redirectAttributes.addFlashAttribute("successMessage", "Successfully changed");
		return "redirect:/client/changeClientInfo";
	}

	public String changePassword(User user,
								 ChangePasswordDTO dto,
								 BindingResult errors,
								 RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			fillErrors(errors, redirectAttributes);
			return "redirect:/client/changeClientInfo";
		}

		if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
			redirectAttributes.addFlashAttribute(
					"oldPasswordErr", "Input your old password");
			return "redirect:/client/changeClientInfo";
		}

		if (!dto.getPasswordFirstTry().equals(dto.getPasswordSecondTry())) {
			redirectAttributes.addFlashAttribute(
					"passwordFirstTryErr", "Confirm password");
			return "redirect:/client/changeClientInfo";
		}

		if (encoder.matches(dto.getPasswordFirstTry(), dto.getOldPassword())) {
			redirectAttributes.addFlashAttribute(
					"passwordFirstTryErr", "Old and new passwords must be various"
			);
			return "redirect:/client/changeClientInfo";
		}

		user.setPassword(encoder.encode(dto.getPasswordFirstTry()));
		userRepository.save(user);
		return "redirect:/login";
	}

}
