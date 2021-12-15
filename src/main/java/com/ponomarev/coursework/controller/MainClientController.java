package com.ponomarev.coursework.controller;

import com.ponomarev.coursework.entity.User;
import com.ponomarev.coursework.service.MainClientService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/client")
@AllArgsConstructor
public class MainClientController {

	private final MainClientService mainClientService;

	@GetMapping
	public String clientMainPage(@AuthenticationPrincipal User user,
								 HttpServletRequest request,
								 Model model) {
		return mainClientService.clientMainPage(user, request, model);
	}

	@PostMapping("/blockCard/{cardNumber}")
	public String blockCard(@AuthenticationPrincipal User user,
							@PathVariable String cardNumber,
							RedirectAttributes redirectAttributes) {
		return mainClientService.blockCard(user, cardNumber, redirectAttributes);
	}

}
