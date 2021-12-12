package com.ponomarev.coursework.controller;


import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.service.HistoryService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;

@Controller
@RequestMapping("/client")
@AllArgsConstructor
public class ClientHistoryController {

	private final HistoryService historyService;

	@GetMapping("/history")
	public String getHistoryPage(@AuthenticationPrincipal User user, Model model) {
		return historyService.historyPage(user, model);
	}

	@GetMapping("/historyByDate")
	public String getHistoryPage(@AuthenticationPrincipal User user,
								 @RequestParam(name = "date") String date,
								 Model model) throws ParseException {
		return historyService.historyPageByDate(user, date, model);
	}

	@GetMapping("/resetHistory")
	public String resetHistory() {
		return "redirect:/client/history";
	}
}
