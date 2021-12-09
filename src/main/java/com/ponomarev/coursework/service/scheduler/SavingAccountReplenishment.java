package com.ponomarev.coursework.service.scheduler;

import com.ponomarev.coursework.model.SavingAccount;
import com.ponomarev.coursework.repository.SavingAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class SavingAccountReplenishment {

	private final SavingAccountRepository savingAccountRepository;

	@Scheduled(cron = "0 0 0 * * ?")
	@Async
	public void doAccrual() {
		int dayOfMonth = LocalDate.now().getDayOfMonth();
		Optional<List<SavingAccount>> allByDay = savingAccountRepository.findAllByDay(dayOfMonth);
		if (allByDay.isPresent()) {
			List<SavingAccount> accounts = allByDay.get();
			accounts.forEach(acc -> {
				acc.setBalance(acc.getBalance() + acc.getMinBalance() * 0.0015);
				acc.setMinBalance(acc.getBalance());
			});
			savingAccountRepository.saveAll(accounts);
		}
	}

}
