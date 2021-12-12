package com.ponomarev.coursework.service;

import com.ponomarev.coursework.model.CardInfo;
import com.ponomarev.coursework.model.Template;
import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.model.UserInfo;
import com.ponomarev.coursework.repository.*;
import com.ponomarev.coursework.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ClientTemplateService implements BaseService {

	private final UserInfoRepository userInfoRepository;

	private final UserRepository userRepository;

	private final CardInfoRepository cardInfoRepository;

	private final TemplateRepository templateRepository;

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

}
