package com.ponomarev.coursework.service.scheduler;

import com.ponomarev.coursework.model.CardInfo;
import com.ponomarev.coursework.repository.CardInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReplenishmentOfAccount {

	private final CardInfoRepository cardInfoRepository;

	@Scheduled(cron = "*/10 * * * * *")
	@Async
	public void upBalance() {
		List<CardInfo> all = cardInfoRepository.findAll();

		List<CardInfo> updatedCardList = all.stream()
				.filter(CardInfo::isActive)
				.peek(card -> card.setBalance(card.getBalance() + 100)).collect(Collectors.toList());

		cardInfoRepository.saveAll(updatedCardList);
	}

}
