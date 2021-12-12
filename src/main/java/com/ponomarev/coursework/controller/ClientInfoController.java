package com.ponomarev.coursework.controller;

import com.ponomarev.coursework.dto.ChangeLoginEmailDTO;
import com.ponomarev.coursework.dto.ChangePasswordDTO;
import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.service.ClientInfoService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/client")
@AllArgsConstructor
public class ClientInfoController {

	private final ClientInfoService clientInfoService;

	@GetMapping("/changeClientInfo")
	public String clientInfoPage(@AuthenticationPrincipal User user,
								 HttpServletRequest request,
								 Model model) {
		return clientInfoService.clientInfoPage(user, request, model);
	}

	@PostMapping("/changeClientInfo")
	public String changeClientInfo(@AuthenticationPrincipal User user,
								   @ModelAttribute @Valid ChangeLoginEmailDTO dto,
								   BindingResult errors,
								   RedirectAttributes redirectAttributes) {
		return clientInfoService.changeClientInfo(user, dto, errors, redirectAttributes);
	}

	@PostMapping("/changeClientPassword")
	public String changeClientPassword(@AuthenticationPrincipal User user,
									   @ModelAttribute @Valid ChangePasswordDTO dto,
									   BindingResult errors,
									   RedirectAttributes redirectAttributes) {
		return clientInfoService.changePassword(user, dto, errors, redirectAttributes);
	}

}
