package com.ponomarev.coursework.repository;

import com.ponomarev.coursework.model.Template;
import com.ponomarev.coursework.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
	Optional<Template> findTemplateByCardNumberAndUser(String  cardNumber, User user);

	@Query("select t from Template t where t.user is null")
	List<Template> findAllWhereUserIsNull();

}
