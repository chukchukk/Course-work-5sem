package com.ponomarev.coursework.controller;

import com.ponomarev.coursework.model.Template;
import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.service.ClientTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/client/templates")
@AllArgsConstructor
public class ClientTemplateController {

	private final ClientTemplateService clientTemplateService;

	@GetMapping
	public String templatesPage(@AuthenticationPrincipal User user,
								HttpServletRequest request,
								Model model) {
		return clientTemplateService.templatesPage(user, request, model);
	}

	@GetMapping("/findClient")
	public String findClientForTemplate(@AuthenticationPrincipal User user,
										@RequestParam(name = "cardNumber") String cardNumber,
										RedirectAttributes redirectAttributes) {
		return clientTemplateService.findClientForTemplate(user, cardNumber, redirectAttributes);
	}

	@PostMapping("/addTemplate")
	public String addClientTemplate(@ModelAttribute Template template,
									@AuthenticationPrincipal User user) {
		return clientTemplateService.addClientTemplate(user, template);
	}

	@PostMapping("/deleteTemplate/{cardNumber}")
	public String deleteTemplate(@PathVariable(name = "cardNumber") String cardNumber,
								 @AuthenticationPrincipal User user) {
		return clientTemplateService.deleteTemplate(cardNumber, user);
	}

}
