package com.ponomarev.coursework.service;

import com.ponomarev.coursework.model.History;
import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.model.UserInfo;
import com.ponomarev.coursework.repository.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HistoryService {

	private final HistoryRepository historyRepository;

	public String historyPage(User user, Model model) {
		UserInfo information = user.getInformation();
		Optional<List<History>> histories = historyRepository.findAllByUserInfo(information);
		if (histories.isPresent()) {
			model.addAttribute("historyList", histories.get());
		} else {
			model.addAttribute("emptyList", "Empty list of operations");
		}
		return "client/operation_history";
	}

	public String historyPageByDate(User user, String date, Model model) throws ParseException {
		if (date.equals("")) {
			model.addAttribute("emptyList", "Incorrect date input");
			return "client/operation_history";
		}
		List<History> allByDate = historyRepository.findAllByDateAndUserInfo(
				new SimpleDateFormat("yyyy-MM-dd").parse(date),
				user.getInformation()
		);
		if (!allByDate.isEmpty()) {
			model.addAttribute("historyList", allByDate);
		} else {
			model.addAttribute("emptyList", "Empty list of operations");
		}
		model.addAttribute("clear", true);
		return "client/operation_history";
	}

}
