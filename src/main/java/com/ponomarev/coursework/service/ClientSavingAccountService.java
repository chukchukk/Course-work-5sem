package com.ponomarev.coursework.service;

import com.ponomarev.coursework.dto.SavingAccountDTO;
import com.ponomarev.coursework.dto.SavingAccountOperationDTO;
import com.ponomarev.coursework.model.CardInfo;
import com.ponomarev.coursework.model.SavingAccount;
import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.repository.CardInfoRepository;
import com.ponomarev.coursework.repository.SavingAccountRepository;
import com.ponomarev.coursework.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class ClientSavingAccountService implements BaseService {

	private final UserRepository userRepository;

	private final CardInfoRepository cardInfoRepository;

	private final SavingAccountRepository savingAccountRepository;

	private final CardsMapper cardsMapper;

	private final HistoryCreationService historyCreationService;

	public String getSavingAccountPage(User user, Model model, HttpServletRequest request) {
		SavingAccount savingAccount = user.getSavingAccount();
		if (savingAccount == null) {
			model.addAttribute("hasAccount", false);
		}
		else {
			SavingAccountDTO savingAccountDTO = SavingAccountDTO.builder()
					.balance(savingAccount.getBalance())
					.minBalance(savingAccount.getMinBalance())
					.accrual(savingAccount.getMinBalance() * 0.015)
					.build();
			model.addAttribute("hasAccount", true);
			model.addAttribute("savingAccount", savingAccountDTO);
		}
		requestModelFilling(request, model);

		return "client/saving_account";
	}

	public String createSavingAccountPage(User user, RedirectAttributes redirectAttributes,
										  HttpServletRequest request) {
		Map<String, ?> flashAttributes = RequestContextUtils.getInputFlashMap(request);
		redirectAttributes.addFlashAttribute("hasAccount", false);
		redirectAttributes.addFlashAttribute("creatingAccount", true);

		redirectAttributes.addFlashAttribute("cards", cardsMapper.getUserCards(user));
		if (flashAttributes != null) {
			flashAttributes.forEach(redirectAttributes::addFlashAttribute);
			System.out.println(redirectAttributes.getFlashAttributes());
		}
		return "redirect:/client/savingAccount";
	}

	public String createSavingAccount(User user, SavingAccountOperationDTO savingAccountDTO, BindingResult errors, RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			fillErrors(errors, redirectAttributes);
			return "redirect:/client/savingAccount/createAccount";
		}
		if (savingAccountDTO.getSum() <= 0) {
			redirectAttributes.addFlashAttribute("sumErr", "Initial sum must be more then 0");
			return "redirect:/client/savingAccount/createAccount";
		}
		Optional<CardInfo> userCard = cardInfoRepository.findCardInfoByCardNumber(savingAccountDTO.getCardNumber());
		if (userCard.isPresent()) {
			CardInfo cardInfo = userCard.get();
			if (cardInfo.getBalance() < savingAccountDTO.getSum()) {
				redirectAttributes.addFlashAttribute("sumErr", "Check your card balance");
			}
			else {
				Date time = Calendar.getInstance().getTime();
				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setCreatedDate(time);
				savingAccount.setDay(LocalDate.now().getDayOfMonth());
				savingAccount.setBalance(savingAccountDTO.getSum());
				savingAccount.setMinBalance(savingAccount.getBalance());
				savingAccount.setActive(true);
				cardInfo.setBalance(0.0);
				cardInfoRepository.save(cardInfo);

				user.setSavingAccount(savingAccount);
				userRepository.save(user);
			}
		} else {
			redirectAttributes.addFlashAttribute("sumErr", "Card is not present");
		}
		return "redirect:/client/savingAccount";
	}

	public String replenishmentAccountPage(User user, Model model, HttpServletRequest request) {
		requestModelFilling(request, model);
		model.addAttribute("cards", cardsMapper.getUserCards(user));
		return "client/replenishment_saving_account";
	}

	public String replenishmentAccount(SavingAccountOperationDTO dto, User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		CardInfo cardInfo = cardInfoRepository.findCardInfoByCardNumber(dto.getCardNumber()).get();
		if (bindingResult.hasErrors()) {
			fillErrors(bindingResult, redirectAttributes);
		} else {
			if (cardInfo.getBalance() < dto.getSum()) {
				redirectAttributes.addFlashAttribute("sumErr", "insufficient funds");
			} else {
				SavingAccount savingAccount = user.getSavingAccount();
				savingAccount.setBalance(savingAccount.getBalance() + dto.getSum());
				cardInfo.setBalance(cardInfo.getBalance() - dto.getSum());
				cardInfoRepository.save(cardInfo);
				savingAccountRepository.save(savingAccount);
				historyCreationService.replenishmentHistory(user.getInformation(), cardInfo.getCardNumber(), dto.getSum());
				redirectAttributes.addFlashAttribute("success", "Success");
			}
		}
		return "redirect:/client/savingAccount/replenishment";
	}

	public String withdrawAccountPage(User user, Model model, HttpServletRequest request) {
		requestModelFilling(request, model);
		model.addAttribute("cards", cardsMapper.getUserCards(user));
		model.addAttribute("savingAccountBalance", user.getSavingAccount().getBalance());
		return "client/withdraw_account";
	}

	public String withdrawAccount(SavingAccountOperationDTO dto, User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		CardInfo cardInfo = cardInfoRepository.findCardInfoByCardNumber(dto.getCardNumber()).get();
		if (bindingResult.hasErrors()) {
			fillErrors(bindingResult, redirectAttributes);
		} else {
			if (cardInfo.getBalance() < dto.getSum()) {
				redirectAttributes.addFlashAttribute("sumErr", "insufficient funds");
			} else {
				SavingAccount savingAccount = user.getSavingAccount();
				cardInfo.setBalance(cardInfo.getBalance() + dto.getSum());
				savingAccount.setBalance(savingAccount.getBalance() - dto.getSum());
				if (savingAccount.getBalance() < savingAccount.getMinBalance()) {
					savingAccount.setMinBalance(savingAccount.getBalance());
				}
				cardInfoRepository.save(cardInfo);
				savingAccountRepository.save(savingAccount);
				historyCreationService.withdrawHistory(user.getInformation(), cardInfo.getCardNumber(), dto.getSum());
				redirectAttributes.addFlashAttribute("success", "Success");
			}
		}
		return "redirect:/client/savingAccount/withdraw";
	}

	public String closeAccountPage(User user, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("isClosing", true);
		redirectAttributes.addFlashAttribute("cards", cardsMapper.getUserCards(user));
		return "redirect:/client/savingAccount";
	}

	public String closeAccount(String cardNumber, User user) {
		CardInfo cardInfoByCardNumber = cardInfoRepository.findCardInfoByCardNumber(cardNumber).get();
		SavingAccount savingAccount = user.getSavingAccount();
		cardInfoByCardNumber.setBalance(cardInfoByCardNumber.getBalance() + savingAccount.getBalance());
		user.setSavingAccount(null);
		savingAccount.setActive(false);
		userRepository.save(user);
		savingAccountRepository.save(savingAccount);
		cardInfoRepository.save(cardInfoByCardNumber);
		historyCreationService.closeSavingAccountHistory(user.getInformation(), cardNumber);
		return "redirect:/client";
	}

}
