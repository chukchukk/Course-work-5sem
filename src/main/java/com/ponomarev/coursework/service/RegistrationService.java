package com.ponomarev.coursework.service;

import com.ponomarev.coursework.model.CardInfo;
import com.ponomarev.coursework.repository.CardInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService implements BaseService{

	private final CardInfoRepository cardInfoRepository;

	private final MailSenderService mailSenderService;

	public String getFirstStepPage(HttpServletRequest request, Model model) {
		requestModelFilling(request, model);
		return "registration/registration_page1";
	}

	public String checkCardAndSend(String cardNumber, RedirectAttributes redirectAttributes) {
		Optional<CardInfo> infoByCardNumber = cardInfoRepository.findCardInfoByCardNumber(cardNumber);

		if (!infoByCardNumber.isPresent()) {
			redirectAttributes.addFlashAttribute("cardNumberErr", "Check your card number");
			return "redirect:/registration/firstStep";
		}
		mailSenderService.sendForRegistration(infoByCardNumber.get().getUserInfo());
		redirectAttributes.addFlashAttribute("mailTo", "mailToMessage");
		return "redirect:/registration/secondStep";
	}

	public String getSecondStepPage(HttpServletRequest request, Model model) {
		requestModelFilling(request, model);
		return "registration/registration_page2";
	}
}
