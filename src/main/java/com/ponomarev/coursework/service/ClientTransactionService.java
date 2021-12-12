package com.ponomarev.coursework.service;

import com.ponomarev.coursework.dto.FromToTransferDTO;
import com.ponomarev.coursework.dto.TransactionDTO;
import com.ponomarev.coursework.model.CardInfo;
import com.ponomarev.coursework.model.Template;
import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.model.UserInfo;
import com.ponomarev.coursework.repository.CardInfoRepository;
import com.ponomarev.coursework.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ClientTransactionService implements BaseService {

	private final UserInfoRepository userInfoRepository;

	private final CardInfoRepository cardInfoRepository;

	private final TransactionMapper transactionMapper;

	private final HistoryCreationService historyCreationService;

	public String getTransactionPage(HttpServletRequest request, Model model) {
		requestModelFilling(request, model);
		return "client/transaction_page";
	}

	//TODO фикс перевода на свою карту
	public String findClientForTransaction(User user, String cardNumber, RedirectAttributes redirectAttributes) {
		UserInfo byUser = userInfoRepository.findByUser(user);
		if (byUser.getCardInfo().stream().map(CardInfo::getCardNumber).anyMatch(cn -> cn.equals(cardNumber))) {
			redirectAttributes.addFlashAttribute("isFounded", true);
			redirectAttributes.addFlashAttribute("transactionClient", new TransactionDTO());
			redirectAttributes.addFlashAttribute("notSuccess", "You cannot transfer money to your card");
			return "redirect:/client/transaction";
		}
		Optional<CardInfo> infoByCardNumber = cardInfoRepository.findCardInfoByCardNumber(cardNumber);
		if (infoByCardNumber.isPresent()) {
			CardInfo transactionCardInfo = infoByCardNumber.get();
			redirectAttributes.addFlashAttribute("isFounded", true);
			redirectAttributes.addFlashAttribute("transactionClient",
					transactionMapper.cardInfoToTransactionDTO(transactionCardInfo, user));
			redirectAttributes.addFlashAttribute("success", "It was found by your request");
		}
		else {
			redirectAttributes.addFlashAttribute("notSuccess", "Not found");
		}
		return  "redirect:/client/transaction";
	}

	public String doTransaction(User user, FromToTransferDTO fromToTransferDTO, BindingResult errors, RedirectAttributes redirectAttributes) {
		UserInfo information = user.getInformation();
		Set<CardInfo> cardInfoSet = information.getCardInfo();
		if (errors.hasErrors()) {
			fillErrors(errors, redirectAttributes);
			redirectAttributes.addFlashAttribute("isFounded", true);
			redirectAttributes.addFlashAttribute("transactionClient",
					transactionMapper.fromToTransferToTransactionDTO(fromToTransferDTO, user));
			return "redirect:/client/transaction";
		}
		boolean userHasCard = cardInfoSet.stream()
				.map(CardInfo::getCardNumber)
				.anyMatch(cardNumber -> cardNumber.equals(fromToTransferDTO.getFromCardNumber()));

		if (userHasCard) {
			CardInfo userCard = cardInfoRepository.findCardInfoByCardNumber(fromToTransferDTO.getFromCardNumber()).get();
			if (userCard.getBalance() < fromToTransferDTO.getSum()) {

				redirectAttributes.addFlashAttribute("transactionClient",
						transactionMapper.fromToTransferToTransactionDTO(fromToTransferDTO, user));
				return "redirect:/client/transaction";
			} else {
				CardInfo toCard = cardInfoRepository.findCardInfoByCardNumber(fromToTransferDTO.getToCardNumber()).get();
				userCard.setBalance(userCard.getBalance() - fromToTransferDTO.getSum());
				toCard.setBalance(toCard.getBalance() + fromToTransferDTO.getSum());
				cardInfoRepository.save(toCard);
				cardInfoRepository.save(userCard);
				historyCreationService.transactionHistoryFrom(information, userCard.getCardNumber(),
						toCard.getCardNumber(), fromToTransferDTO.getSum());
				historyCreationService.transactionHistoryTo(toCard.getUserInfo(), userCard.getCardNumber(),
						toCard.getCardNumber(), fromToTransferDTO.getSum());
				redirectAttributes.addFlashAttribute("transactionIsEnded", "Success");
			}
		} else {
			redirectAttributes.addFlashAttribute("transactionClient",
					transactionMapper.fromToTransferToTransactionDTO(fromToTransferDTO, user));
			redirectAttributes.addFlashAttribute("balanceErr", "Insufficient funds on the balance sheet");
		}
		return "redirect:/client/transaction";
	}

	public String transactionFromTemplate(User user, Template template, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("isFounded", true);
		redirectAttributes.addFlashAttribute("transactionClient",
				transactionMapper.templateToTransactionDTO(template, user));
		return "redirect:/client/transaction";
	}

}
