package com.ponomarev.coursework.controller;

import com.ponomarev.coursework.dto.UserDTO;
import com.ponomarev.coursework.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

	@GetMapping("/completion")
	public String getSecondStepPage(HttpServletRequest httpServletRequest, Model model) {
		return registrationService.getSecondStepPage(httpServletRequest, model);
	}

	@PostMapping("/checkConfirmationCode/{id}")
	public String checkConfirmationCode(@RequestParam(name = "confirmationCode") String confirmationCode,
										@PathVariable Long id,
										RedirectAttributes redirectAttributes) {
		return registrationService.checkConfirmationCode(id, confirmationCode, redirectAttributes);
	}

	@PostMapping("/createUser/{id}")
	public String createUser(@ModelAttribute @Valid UserDTO userDTO,
							 @PathVariable Long id,
							 BindingResult bindingResult,
							 RedirectAttributes redirectAttributes) {
		return registrationService.createUser(userDTO, id, bindingResult, redirectAttributes);
	}
}
