package com.ponomarev.coursework.service;

import com.ponomarev.coursework.model.ConfirmationToken;
import com.ponomarev.coursework.model.UserInfo;
import com.ponomarev.coursework.repository.redis.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailSenderService {

	private final ConfirmationTokenRepository confirmationTokenRepository;

	@Value("${spring.mail.username}")
	private String mailFrom;

	private final JavaMailSender mailSender;

	@Async
	public String sendForRegistration(UserInfo userInfo) {
		String confirmUUID = UUID.randomUUID().toString();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(mailFrom);
		message.setTo(userInfo.getEmail());
		message.setSubject("Confirmation code from bank");
		message.setText("Your confirmation code : " + confirmUUID + "\nCopy it before close this message.");
		mailSender.send(message);

		ConfirmationToken confirmationToken = ConfirmationToken
				.builder()
				.id(userInfo.getId().toString())
				.confirmationToken(confirmUUID)
				.build();
		confirmationTokenRepository.save(confirmationToken);
		return "A confirmation code has been sent to the address " + userInfo.getEmail();
	}

}
