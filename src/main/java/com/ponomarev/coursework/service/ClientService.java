package com.ponomarev.coursework.service;

import com.ponomarev.coursework.dto.ChangeLoginEmailDTO;
import com.ponomarev.coursework.dto.ChangePasswordDTO;
import com.ponomarev.coursework.dto.TransactionDTO;
import com.ponomarev.coursework.model.CardInfo;
import com.ponomarev.coursework.model.Template;
import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.model.UserInfo;
import com.ponomarev.coursework.repository.CardInfoRepository;
import com.ponomarev.coursework.repository.TemplateRepository;
import com.ponomarev.coursework.repository.UserInfoRepository;
import com.ponomarev.coursework.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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
			redirectAttributes.addFlashAttribute("cardSuccess", "Successfully deleted");
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

	public String addClientTemplate(User user,Template template,
									RedirectAttributes redirectAttributes) {
		Set<Template> templates = user.getTemplates();
		templates.add(template);
		userRepository.save(user);
		return "redirect:/client/templates";
	}

	public String deleteTemplate(String cardNumber, User user) {
		Optional<Template> template = templateRepository.findTemplateByCardNumberAndUser(cardNumber, user);
		templateRepository.delete(template.get());
		return "redirect:/client/templates";
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
			redirectAttributes.addFlashAttribute("notSuccessSearch", "You cannot transfer money to your card");
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
			cardInfos.stream().forEach(cardInfo -> cards.put(cardInfo.getCardNumber(), cardInfo.getBalance() + " Р"));
			transactionDTO.setCards(cards);

			redirectAttributes.addFlashAttribute("isFounded", true);
			redirectAttributes.addFlashAttribute("transactionClient", transactionDTO);
			redirectAttributes.addFlashAttribute("successSearch", "It was found by your request");
		}
		return  "redirect:/client/transaction";
	}

}
