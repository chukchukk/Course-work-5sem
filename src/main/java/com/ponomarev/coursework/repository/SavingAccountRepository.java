package com.ponomarev.coursework.repository;

import com.ponomarev.coursework.model.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavingAccountRepository extends JpaRepository<SavingAccount, Long> {
	Optional<List<SavingAccount>> findAllByDay(Integer day);
}
