package com.ponomarev.coursework.service.scheduler;

import com.ponomarev.coursework.entity.Template;
import com.ponomarev.coursework.repository.TemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmptyTemplateRemoving {

	private final TemplateRepository templateRepository;

	@Scheduled(cron = "*/10 * * * * *")
	@Async
	public void deleteTemplates() {
		List<Template> allWhereUserIsNull = templateRepository.findAllWhereUserIsNull();
		templateRepository.deleteAll(allWhereUserIsNull);
	}

}
