package com.ponomarev.coursework.service;

import com.ponomarev.coursework.entity.*;
import com.ponomarev.coursework.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MainClientService implements BaseService {

	private final UserInfoRepository userInfoRepository;

	private final CardInfoRepository cardInfoRepository;

	private final HistoryCreationService historyCreationService;

	public String clientMainPage(User user, HttpServletRequest request, Model model) {
		requestModelFilling(request, model);
		UserInfo userInfo = userInfoRepository.findByUser(user);
		List<CardInfo> cardInfoList = userInfo.getCardInfo().stream().filter(CardInfo::isActive).collect(Collectors.toList());
		model.addAttribute("cards", cardInfoList);
		return "client/main";
	}

	public String blockCard(User user, String cardNumber, RedirectAttributes redirectAttributes) {
		Optional<CardInfo> optionalCardInfo = cardInfoRepository.findCardInfoByCardNumber(cardNumber);
		if (optionalCardInfo.isPresent()) {
			CardInfo cardInfo = optionalCardInfo.get();
			cardInfo.setActive(false);
			cardInfoRepository.save(cardInfo);
			redirectAttributes.addFlashAttribute("cardSuccess",
					"Successfully blocked");
			historyCreationService.closeCardHistory(user.getInformation(), cardInfo.getCardNumber());
		} else {
			redirectAttributes.addFlashAttribute("cardErr",
					"Something went wrong, please try again later");
		}
		return "redirect:/client";
	}

}
