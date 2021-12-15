package com.ponomarev.coursework.service;

import com.ponomarev.coursework.entity.History;
import com.ponomarev.coursework.entity.UserInfo;
import com.ponomarev.coursework.repository.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

@Service
@AllArgsConstructor
public class HistoryCreationService {

	private final HistoryRepository historyRepository;

	public void transactionHistoryFrom(UserInfo userInfo, String from, String to, Double sum) {
		Date time = Calendar.getInstance().getTime();
		History history = new History();
		history.setDate(time);
		history.setOperationType(Collections.singleton(History.OperationType.TRANSFER));
		history.setMessage("Transfer from card " + from + " to card " + to + " in the amount of " + sum + ".");
		history.setUserInfo(userInfo);
		historyRepository.save(history);
	}

	public void transactionHistoryTo(UserInfo userInfo, String from, String to, Double sum) {
		Date time = Calendar.getInstance().getTime();
		History history = new History();
		history.setDate(time);
		history.setOperationType(Collections.singleton(History.OperationType.TRANSFER));
		history.setMessage("Receiving from card " + from + " to card " + to + " in the amount of " + sum + ".");
		history.setUserInfo(userInfo);
		historyRepository.save(history);
	}

	public void replenishmentHistory(UserInfo userInfo, String cardFrom, Double sum) {
		Date time = Calendar.getInstance().getTime();
		History history = new History();
		history.setDate(time);
		history.setOperationType(Collections.singleton(History.OperationType.SAVING_ACCOUNT_OPERATION));
		history.setMessage("Top up savings account for " + sum + " from " + cardFrom + " card number.");
		history.setUserInfo(userInfo);
		historyRepository.save(history);
	}

	public void withdrawHistory(UserInfo userInfo, String cardTo, Double sum) {
		Date time = Calendar.getInstance().getTime();
		History history = new History();
		history.setDate(time);
		history.setOperationType(Collections.singleton(History.OperationType.SAVING_ACCOUNT_OPERATION));
		history.setMessage("Withdrawing from saving account to " + cardTo + " in the amount of " + sum + ".");
		history.setUserInfo(userInfo);
		historyRepository.save(history);
	}

	public void closeSavingAccountHistory(UserInfo userInfo, String cardTo) {
		Date time = Calendar.getInstance().getTime();
		History history = new History();
		history.setDate(time);
		history.setOperationType(Collections.singleton(History.OperationType.SAVING_ACCOUNT_OPERATION));
		history.setMessage("Closing a savings account. The entire balance has been transferred to " + cardTo + ".");
		history.setUserInfo(userInfo);
		historyRepository.save(history);
	}

	public void closeCardHistory(UserInfo userInfo, String cardNumber) {
		Date time = Calendar.getInstance().getTime();
		History history = new History();
		history.setDate(time);
		history.setOperationType(Collections.singleton(History.OperationType.CARD_OPERATION));
		history.setMessage("Closed card with number " + cardNumber + ".");
		history.setUserInfo(userInfo);
		historyRepository.save(history);
	}

}
