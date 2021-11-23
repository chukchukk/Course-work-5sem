package com.ponomarev.coursework.controller;

import com.ponomarev.coursework.service.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

	private final RegistrationService registrationService;

	@GetMapping("/firstStep")
	public String getFirstStepPage(HttpServletRequest request, Model model) {
		return registrationService.getFirstStepPage(request, model);
	}

	@PostMapping("/firstStep")
	public String checkCardAndSend(@RequestParam(name = "cardNumber") String cardNumber,
								   RedirectAttributes redirectAttributes) {
		return registrationService.checkCardAndSend(cardNumber, redirectAttributes);
	}

	@GetMapping("/secondStep")
	public String getSecondStepPage(HttpServletRequest httpServletRequest, Model model) {
		return registrationService.getSecondStepPage(httpServletRequest, model);
	}
}
