package com.ponomarev.coursework.repository;

import com.ponomarev.coursework.model.Template;
import com.ponomarev.coursework.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
	Optional<Template> findTemplateByCardNumberAndUser(String  cardNumber, User user);
}
