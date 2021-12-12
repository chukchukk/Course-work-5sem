package com.ponomarev.coursework.service;

import com.ponomarev.coursework.model.CardInfo;
import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.model.UserInfo;
import com.ponomarev.coursework.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class CardsMapper {

	private final UserInfoRepository userInfoRepository;

	public Map<String, String> getUserCards(User user) {
		UserInfo userInfo = userInfoRepository.findByUser(user);
		Set<CardInfo> cardInfoSet = userInfo.getCardInfo();
		Map<String, String> cards = new HashMap<>();
		cardInfoSet.stream().filter(CardInfo::isActive).forEach(cardInfo -> cards.put(cardInfo.getCardNumber(), cardInfo.getBalance() + " Р"));
		return cards;
	}

}
