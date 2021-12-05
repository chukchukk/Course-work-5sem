package com.ponomarev.coursework.service;

import com.ponomarev.coursework.dto.*;
import com.ponomarev.coursework.model.*;
import com.ponomarev.coursework.repository.CardInfoRepository;
import com.ponomarev.coursework.repository.TemplateRepository;
import com.ponomarev.coursework.repository.UserInfoRepository;
import com.ponomarev.coursework.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientService implements BaseService {

	private final UserInfoRepository userInfoRepository;

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder encoder;

	private final CardInfoRepository cardInfoRepository;

	private final TemplateRepository templateRepository;

	public String clientMainPage(User user, HttpServletRequest request, Model model) {
		requestModelFilling(request, model);
		UserInfo userInfo = userInfoRepository.findByUser(user);
		List<CardInfo> cardInfoList = userInfo.getCardInfo().stream().filter(info -> info.isActive()).collect(Collectors.toList());
		model.addAttribute("cards", cardInfoList);
		return "client/main";
	}

	public String clientInfoPage(User user, HttpServletRequest request, Model model) {
		requestModelFilling(request, model);
		UserInfo userInfo = userInfoRepository.findByUser(user);
		model.addAttribute("login", user.getLogin());
		model.addAttribute("email", userInfo.getEmail());
		return "client/client_info";
	}

	public String changeClientInfo(User user,
								   ChangeLoginEmailDTO dto,
								   BindingResult errors,
								   RedirectAttributes redirectAttributes) {
		if (dto.getEmail().isEmpty() && dto.getLogin().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Type new login or email");
			return "redirect:/client/changeClientInfo";
		}
		if (errors.hasErrors()) {
			fillErrors(errors, redirectAttributes);
			redirectAttributes.addFlashAttribute("currentLogin", dto.getLogin());
			redirectAttributes.addFlashAttribute("currentEmail", dto.getEmail());
			return "redirect:/client/changeClientInfo";
		}
		if (!dto.getLogin().isEmpty()) {
			user.setLogin(dto.getLogin());
			userRepository.save(user);
		}
		if (!dto.getEmail().isEmpty()) {
			UserInfo userInfo = userInfoRepository.findByUser(user);
			userInfo.setEmail(dto.getEmail());
			userInfoRepository.save(userInfo);
		}
		redirectAttributes.addFlashAttribute("successMessage", "Successfully changed");
		return "redirect:/client/changeClientInfo";
	}

	public String changePassword(User user,
								 ChangePasswordDTO dto,
								 BindingResult errors,
								 RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			fillErrors(errors, redirectAttributes);
			return "redirect:/client/changeClientInfo";
		}

		if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
			redirectAttributes.addFlashAttribute(
					"oldPasswordErr", "Input your old password");
			return "redirect:/client/changeClientInfo";
		}

		if (!dto.getPasswordFirstTry().equals(dto.getPasswordSecondTry())) {
			redirectAttributes.addFlashAttribute(
					"passwordFirstTryErr", "Confirm password");
			return "redirect:/client/changeClientInfo";
		}

		if (encoder.matches(dto.getPasswordFirstTry(), dto.getOldPassword())) {
			redirectAttributes.addFlashAttribute(
					"passwordFirstTryErr", "Old and new passwords must be various"
			);
			return "redirect:/client/changeClientInfo";
		}

		user.setPassword(encoder.encode(dto.getPasswordFirstTry()));
		userRepository.save(user);
		return "redirect:/login";
	}

	public String blockCard(String cardNumber, RedirectAttributes redirectAttributes) {
		Optional<CardInfo> optionalCardInfo = cardInfoRepository.findCardInfoByCardNumber(cardNumber);
		if (optionalCardInfo.isPresent()) {
			CardInfo cardInfo = optionalCardInfo.get();
			cardInfo.setActive(false);
			cardInfoRepository.save(cardInfo);
			redirectAttributes.addFlashAttribute("cardSuccess", "Successfully blocked");
		} else {
			redirectAttributes.addFlashAttribute("cardErr",
					"Something went wrong, please try again later");
		}
		return "redirect:/client";
	}

	public String templatesPage(User user, HttpServletRequest request, Model model) {
		requestModelFilling(request, model);
		Set<Template> templates = user.getTemplates();
		model.addAttribute("clients", templates);
		return "client/templates";
	}

	public String findClientForTemplate(User user,
										String cardNumber,
										RedirectAttributes redirectAttributes) {
		UserInfo byUser = userInfoRepository.findByUser(user);
		if (byUser.getCardInfo().stream().map(CardInfo::getCardNumber).anyMatch(cn -> cn.equals(cardNumber))) {
			redirectAttributes.addFlashAttribute("flag", true);
			redirectAttributes.addFlashAttribute("clientByCardNumber", new Template());
			redirectAttributes.addFlashAttribute("notSuccessSearch", "You cannot add your own card to templates");
			return "redirect:/client/templates";
		}
		if (templateRepository.findTemplateByCardNumberAndUser(cardNumber, user).isPresent()) {
			redirectAttributes.addFlashAttribute("flag", true);
			redirectAttributes.addFlashAttribute("clientByCardNumber", new Template());
			redirectAttributes.addFlashAttribute("notSuccessSearch", "Client has already been added to the template");
			return "redirect:/client/templates";
		}
		Optional<CardInfo> infoByCardNumber = cardInfoRepository.findCardInfoByCardNumber(cardNumber);
		if (infoByCardNumber.isPresent()) {
			CardInfo cardInfo = infoByCardNumber.get();
			UserInfo userInfo = cardInfo.getUserInfo();
			Template template = new Template();
			template.setCardNumber(cardInfo.getCardNumber());
			template.setFirstName(userInfo.getFirstName());
			template.setLastName(userInfo.getLastName());
			redirectAttributes.addFlashAttribute("flag", true);
			redirectAttributes.addFlashAttribute("clientByCardNumber", template);
			redirectAttributes.addFlashAttribute("successSearch", "It was found by your request");
		} else {
			redirectAttributes.addFlashAttribute("flag", true);
			redirectAttributes.addFlashAttribute("notSuccessSearch", "Nothing was found for your request");
			redirectAttributes.addFlashAttribute("clientByCardNumber", new Template());
		}
		return "redirect:/client/templates";
	}

	public String addClientTemplate(User user,Template template) {
		Set<Template> templates = user.getTemplates();
		templates.add(template);
		userRepository.save(user);
		return "redirect:/client/templates";
	}

	public String deleteTemplate(String cardNumber, User user) {
		Template dbTemplate = templateRepository.findTemplateByCardNumberAndUser(cardNumber, user).get();
		user.deleteTemplateById(dbTemplate.getId());
		userRepository.save(user);
		return "redirect:/client";
	}

	public String getTransactionPage(HttpServletRequest request, Model model) {
		requestModelFilling(request, model);
		return "client/transaction_page";
	}

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
			TransactionDTO transactionDTO = new TransactionDTO();
			CardInfo transactionCardInfo = infoByCardNumber.get();
			UserInfo transactionUserInfo = transactionCardInfo.getUserInfo();
			transactionDTO.setCardNumber(transactionCardInfo.getCardNumber());
			transactionDTO.setFirstName(transactionUserInfo.getFirstName());
			transactionDTO.setLastName(transactionUserInfo.getLastName());

			Set<CardInfo> cardInfos = byUser.getCardInfo();
			Map<String, String> cards = new HashMap<>();
			cardInfos.stream().filter(CardInfo::isActive).forEach(cardInfo -> cards.put(cardInfo.getCardNumber(), cardInfo.getBalance() + " Р"));
			transactionDTO.setCards(cards);

			redirectAttributes.addFlashAttribute("isFounded", true);
			redirectAttributes.addFlashAttribute("transactionClient", transactionDTO);
			redirectAttributes.addFlashAttribute("success", "It was found by your request");
		}
		return  "redirect:/client/transaction";
	}

	//TODO рефактор повторяющегося кода, написать класс-helper
	@Transactional(rollbackFor = Exception.class)
	public String doTransaction(User user, FromToTransferDTO fromToTransferDTO, BindingResult errors, RedirectAttributes redirectAttributes) {
		UserInfo information = user.getInformation();
		Set<CardInfo> cardInfoSet = information.getCardInfo();
		if (errors.hasErrors()) {
			fillErrors(errors, redirectAttributes);
			redirectAttributes.addFlashAttribute("isFounded", true);
			TransactionDTO transactionDTO = new TransactionDTO();
			transactionDTO.setFirstName(fromToTransferDTO.getToFirstName());
			transactionDTO.setLastName(fromToTransferDTO.getToLastName());
			transactionDTO.setCardNumber(fromToTransferDTO.getToCardNumber());
			Map<String, String> cards = new HashMap<>();
			cardInfoSet.forEach(cardInfo -> cards.put(cardInfo.getCardNumber(), cardInfo.getBalance() + " Р"));
			transactionDTO.setCards(cards);
			redirectAttributes.addFlashAttribute("transactionClient", transactionDTO);
			return "redirect:/client/transaction";
		}
		boolean userHasCard = cardInfoSet.stream()
				.map(CardInfo::getCardNumber)
				.anyMatch(cardNumber -> cardNumber.equals(fromToTransferDTO.getFromCardNumber()));

		if (userHasCard) {
			CardInfo userCard = cardInfoRepository.findCardInfoByCardNumber(fromToTransferDTO.getFromCardNumber()).get();
			if (userCard.getBalance() < Double.parseDouble(fromToTransferDTO.getSum())) {
				TransactionDTO transactionDTO = new TransactionDTO();
				transactionDTO.setFirstName(fromToTransferDTO.getToFirstName());
				transactionDTO.setLastName(fromToTransferDTO.getToLastName());
				transactionDTO.setCardNumber(fromToTransferDTO.getToCardNumber());
				Map<String, String> cards = new HashMap<>();
				cardInfoSet.stream().filter(CardInfo::isActive).forEach(cardInfo -> cards.put(cardInfo.getCardNumber(), cardInfo.getBalance() + " Р"));
				transactionDTO.setCards(cards);
				redirectAttributes.addFlashAttribute("transactionClient", transactionDTO);
				return "redirect:/client/transaction";
			} else {
				CardInfo toCard = cardInfoRepository.findCardInfoByCardNumber(fromToTransferDTO.getToCardNumber()).get();
				userCard.setBalance(userCard.getBalance() - Double.parseDouble(fromToTransferDTO.getSum()));
				toCard.setBalance(toCard.getBalance() + Double.parseDouble(fromToTransferDTO.getSum()));
				redirectAttributes.addFlashAttribute("transactionIsEnded", "Success");
			}
		} else {
			TransactionDTO transactionDTO = new TransactionDTO();
			transactionDTO.setFirstName(fromToTransferDTO.getToFirstName());
			transactionDTO.setLastName(fromToTransferDTO.getToLastName());
			transactionDTO.setCardNumber(fromToTransferDTO.getToCardNumber());
			Map<String, String> cards = new HashMap<>();
			cardInfoSet.stream().filter(CardInfo::isActive).forEach(cardInfo -> cards.put(cardInfo.getCardNumber(), cardInfo.getBalance() + " Р"));
			transactionDTO.setCards(cards);
			redirectAttributes.addFlashAttribute("transactionClient", transactionDTO);
			redirectAttributes.addFlashAttribute("balanceErr", "Insufficient funds on the balance sheet");
		}
		return "redirect:/client/transaction";
	}

	//TODO рефактор повторяющегося кода, написать класс-helper
	public String transactionFromTemplate(User user, Template template, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("isFounded", true);
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setLastName(template.getLastName());
		transactionDTO.setFirstName(template.getFirstName());
		transactionDTO.setCardNumber(template.getCardNumber());
		UserInfo information = user.getInformation();
		Set<CardInfo> cardInfoSet = information.getCardInfo();
		Map<String, String> cards = new HashMap<>();
		cardInfoSet.stream().filter(CardInfo::isActive).forEach(cardInfo -> cards.put(cardInfo.getCardNumber(), cardInfo.getBalance() + " Р"));
		transactionDTO.setCards(cards);
		redirectAttributes.addFlashAttribute("transactionClient", transactionDTO);
		return "redirect:/client/transaction";
	}

	public String getSavingAccountPage(User user, Model model, HttpServletRequest request) {
		Map<String, ?> flashAttributes = RequestContextUtils.getInputFlashMap(request);
		if (flashAttributes == null) {
			SavingAccount savingAccount = user.getSavingAccount();
			if (savingAccount == null) {
				model.addAttribute("hasAccount", false);
			}
			else {
				//TODO
			}
		} else {
			requestModelFilling(request, model);
		}
		return "client/saving_account";
	}

	public String createSavingAccountPage(User user, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("hasAccount", false);
		redirectAttributes.addFlashAttribute("creatingAccount", true);

		//TODO рефактор в хелпер класс
		UserInfo information = user.getInformation();
		Set<CardInfo> cardInfoSet = information.getCardInfo();
		Map<String, String> cards = new HashMap<>();
		cardInfoSet.stream().filter(CardInfo::isActive).forEach(cardInfo -> cards.put(cardInfo.getCardNumber(), cardInfo.getBalance() + " Р"));
		redirectAttributes.addFlashAttribute("cards", cards);
		return "redirect:/client/savingAccount";
	}

	@Transactional(rollbackFor = Exception.class)
	public String createSavingAccount(User user, CreateSavingAccountDTO savingAccountDTO, RedirectAttributes redirectAttributes) {
		if (Double.parseDouble(savingAccountDTO.getSum()) <= 0) {
			redirectAttributes.addFlashAttribute("sumErr", "Initial sum must be more then 0");
			return "redirect:/savingAccount/createAccount";
		}
		Optional<CardInfo> userCard = cardInfoRepository.findCardInfoByCardNumber(savingAccountDTO.getFromCardNumber());
		if (userCard.isPresent()) {
			CardInfo cardInfo = userCard.get();
			if (cardInfo.getBalance() < Double.parseDouble(savingAccountDTO.getSum())) {
				redirectAttributes.addFlashAttribute("sumErr", "Check your card balance");
			}
			else {
				LocalDate date = LocalDate.now();
				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setCreatedDate(date.toString());
				savingAccount.setUpdatedDate(date.toString());
				savingAccount.setBalance(Double.parseDouble(savingAccountDTO.getSum()));
				cardInfo.setBalance(cardInfo.getBalance() - Double.parseDouble(savingAccountDTO.getSum()));

				user.setSavingAccount(savingAccount);
				userRepository.save(user);
				cardInfoRepository.save(cardInfo);
			}
		} else {
			redirectAttributes.addFlashAttribute("sumErr", "Card is not present");
		}
		return "redirect:/client/savingAccount";
	}
}
