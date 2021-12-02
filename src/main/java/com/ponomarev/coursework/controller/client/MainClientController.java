package com.ponomarev.coursework.controller.client;

import com.ponomarev.coursework.dto.ChangeLoginEmailDTO;
import com.ponomarev.coursework.dto.ChangePasswordDTO;
import com.ponomarev.coursework.model.Template;
import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.service.ClientService;
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
public class MainClientController {

	private final ClientService clientService;

	@GetMapping
	public String clientMainPage(@AuthenticationPrincipal User user,
								 HttpServletRequest request,
								 Model model) {
		return clientService.clientMainPage(user, request, model);
	}

	@GetMapping("/changeClientInfo")
	public String clientInfoPage(@AuthenticationPrincipal User user,
								 HttpServletRequest request,
								 Model model) {
		return clientService.clientInfoPage(user, request, model);
	}

	@PostMapping("/changeClientInfo")
	public String changeClientInfo(@AuthenticationPrincipal User user,
								   @ModelAttribute @Valid ChangeLoginEmailDTO dto,
								   BindingResult errors,
								   RedirectAttributes redirectAttributes) {
		return clientService.changeClientInfo(user, dto, errors, redirectAttributes);
	}

	@PostMapping("/changeClientPassword")
	public String changeClientPassword(@AuthenticationPrincipal User user,
									   @ModelAttribute @Valid ChangePasswordDTO dto,
									   BindingResult errors,
									   RedirectAttributes redirectAttributes) {
		return clientService.changePassword(user, dto, errors, redirectAttributes);
	}

	@PostMapping("/blockCard/{cardNumber}")
	public String blockCard(@PathVariable String cardNumber,
							RedirectAttributes redirectAttributes) {
		return clientService.blockCard(cardNumber, redirectAttributes);
	}

	@GetMapping("/templates")
	public String templatesPage(@AuthenticationPrincipal User user,
								HttpServletRequest request,
								Model model) {
		return clientService.templatesPage(user, request, model);
	}

	@GetMapping("/templates/findClient")
	public String findClientForTemplate(@AuthenticationPrincipal User user,
										@RequestParam(name = "cardNumber") String cardNumber,
										RedirectAttributes redirectAttributes) {
		return clientService.findClientForTemplate(user, cardNumber, redirectAttributes);
	}

	@PostMapping("/templates/addTemplate")
	public String addClientTemplate(@ModelAttribute Template template,
									RedirectAttributes redirectAttributes,
									@AuthenticationPrincipal User user) {
		return clientService.addClientTemplate(user, template, redirectAttributes);
	}

	@DeleteMapping("/templates/delete/{cardNumber}")
	public String deleteTemplate(@PathVariable(name = "cardNumber") String cardNumber,
								 @AuthenticationPrincipal User user) {
		return clientService.deleteTemplate(cardNumber, user);
	}

	@GetMapping("/transaction")
	public String getTransactionPage(HttpServletRequest request,
									 Model model) {
		return clientService.getTransactionPage(request, model);
	}

	@GetMapping("/transaction/findClient")
	public String findTransactionClient(@AuthenticationPrincipal User user,
										@RequestParam(name = "cardNumber") String cardNumber,
										RedirectAttributes redirectAttributes) {
		return clientService.findClientForTransaction(user, cardNumber, redirectAttributes);
	}

	//TODO сделать получение карты пользователя при транзакции
	@PostMapping("/transaction/test")
	public String test(@RequestParam(name = "carddd") String carddd) {
		return carddd;
	}

}
