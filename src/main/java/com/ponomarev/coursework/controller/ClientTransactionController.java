package com.ponomarev.coursework.controller;

import com.ponomarev.coursework.dto.FromToTransferDTO;
import com.ponomarev.coursework.entity.Template;
import com.ponomarev.coursework.entity.User;
import com.ponomarev.coursework.service.ClientTransactionService;
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
@RequestMapping("/client")
@AllArgsConstructor
public class ClientTransactionController {

	private final ClientTransactionService clientTransactionService;

	@GetMapping("/transaction")
	public String getTransactionPage(HttpServletRequest request,
									 Model model) {
		return clientTransactionService.getTransactionPage(request, model);
	}

	@GetMapping("/transaction/findClient")
	public String findTransactionClient(@AuthenticationPrincipal User user,
										@RequestParam(name = "cardNumber") String cardNumber,
										RedirectAttributes redirectAttributes) {
		return clientTransactionService.findClientForTransaction(user, cardNumber, redirectAttributes);
	}

	@PostMapping("/transaction/doTransaction")
	public String doTransaction(@AuthenticationPrincipal User user,
								@ModelAttribute @Valid FromToTransferDTO fromToTransferDTO,
								BindingResult errors,
								RedirectAttributes redirectAttributes) {
		return clientTransactionService.doTransaction(user, fromToTransferDTO, errors, redirectAttributes);
	}

	@GetMapping("/transactionFromTemplate")
	public String transactionFromTemplate(@AuthenticationPrincipal User user,
										  @ModelAttribute Template template,
										  RedirectAttributes redirectAttributes) {
		return clientTransactionService.transactionFromTemplate(user, template, redirectAttributes);
	}

}
