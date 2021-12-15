package com.ponomarev.coursework.controller;

import com.ponomarev.coursework.dto.SavingAccountOperationDTO;
import com.ponomarev.coursework.entity.User;
import com.ponomarev.coursework.service.ClientSavingAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/client/savingAccount")
@AllArgsConstructor
public class ClientSavingAccountController {

	private final ClientSavingAccountService clientSavingAccountService;

	@GetMapping
	public String savingAccountPage(@AuthenticationPrincipal User user,
									Model model,
									HttpServletRequest request) {
		return clientSavingAccountService.getSavingAccountPage(user, model, request);
	}

	@GetMapping("/createAccount")
	public String createSavingAccountPage(@AuthenticationPrincipal User user,
										  RedirectAttributes redirectAttributes,
										  HttpServletRequest request) {
		return clientSavingAccountService.createSavingAccountPage(user, redirectAttributes, request);
	}

	@PostMapping("/createAccount")
	public String createSavingAccount(@AuthenticationPrincipal User user,
									  @ModelAttribute @Valid SavingAccountOperationDTO savingAccountDTO,
									  BindingResult bindingResult,
									  RedirectAttributes redirectAttributes) {
		return clientSavingAccountService.createSavingAccount(user, savingAccountDTO, bindingResult, redirectAttributes);
	}

	@GetMapping("/replenishment")
	public String getReplenishmentAccountPage(@AuthenticationPrincipal User user,
											  Model model,
											  HttpServletRequest request) {
		return clientSavingAccountService.replenishmentAccountPage(user, model, request);
	}

	@PostMapping("/replenishment")
	public String replenishmentAccount(@ModelAttribute @Valid SavingAccountOperationDTO dto,
									   @AuthenticationPrincipal User user,
									   BindingResult bindingResult,
									   RedirectAttributes redirectAttributes) {
		return clientSavingAccountService.replenishmentAccount(dto, user, bindingResult, redirectAttributes);
	}

	@GetMapping("/withdraw")
	public String getWithdrawAccountPage(@AuthenticationPrincipal User user,
										 Model model,
										 HttpServletRequest request) {
		return clientSavingAccountService.withdrawAccountPage(user, model, request);
	}

	@PostMapping("/withdraw")
	public String withdrawAccount(@ModelAttribute @Valid SavingAccountOperationDTO dto,
								  @AuthenticationPrincipal User user,
								  BindingResult bindingResult,
								  RedirectAttributes redirectAttributes) {
		return clientSavingAccountService.withdrawAccount(dto, user, bindingResult, redirectAttributes);
	}

	@GetMapping("/close")
	public String getCloseAccountPage(@AuthenticationPrincipal User user,
									  RedirectAttributes redirectAttributes) {
		return clientSavingAccountService.closeAccountPage(user, redirectAttributes);
	}

	@PostMapping("/close")
	public String closeAccount(@RequestParam(name = "cardNumber") String cardNumber,
							   @AuthenticationPrincipal User user) {
		return clientSavingAccountService.closeAccount(cardNumber, user);
	}

}
