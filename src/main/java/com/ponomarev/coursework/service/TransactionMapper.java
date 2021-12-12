package com.ponomarev.coursework.service;

import com.ponomarev.coursework.dto.FromToTransferDTO;
import com.ponomarev.coursework.dto.TransactionDTO;
import com.ponomarev.coursework.model.CardInfo;
import com.ponomarev.coursework.model.Template;
import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.model.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionMapper {

	private final CardsMapper cardsMapper;

	public TransactionDTO fromToTransferToTransactionDTO(FromToTransferDTO fromToTransferDTO, User user) {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setFirstName(fromToTransferDTO.getToFirstName());
		transactionDTO.setLastName(fromToTransferDTO.getToLastName());
		transactionDTO.setCardNumber(fromToTransferDTO.getToCardNumber());
		transactionDTO.setCards(cardsMapper.getUserCards(user));
		return transactionDTO;
	}

	public TransactionDTO templateToTransactionDTO(Template template, User user) {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setLastName(template.getLastName());
		transactionDTO.setFirstName(template.getFirstName());
		transactionDTO.setCardNumber(template.getCardNumber());
		transactionDTO.setCards(cardsMapper.getUserCards(user));
		return transactionDTO;
	}

	public TransactionDTO cardInfoToTransactionDTO(CardInfo cardInfo, User user) {
		TransactionDTO transactionDTO = new TransactionDTO();
		UserInfo transactionUserInfo = cardInfo.getUserInfo();
		transactionDTO.setCardNumber(cardInfo.getCardNumber());
		transactionDTO.setFirstName(transactionUserInfo.getFirstName());
		transactionDTO.setLastName(transactionUserInfo.getLastName());
		transactionDTO.setCards(cardsMapper.getUserCards(user));
		return transactionDTO;
	}

}
