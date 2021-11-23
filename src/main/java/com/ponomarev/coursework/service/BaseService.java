package com.ponomarev.coursework.service;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface BaseService {

	default void fillErrors(BindingResult errors, RedirectAttributes redirectAttributes) {
		List<FieldError> listOfErrors = errors.getFieldErrors();
		for (FieldError f : listOfErrors) {
			redirectAttributes.addFlashAttribute(f.getField() + "Err",
					f.getDefaultMessage());
		}
	}

	default void requestModelFilling(HttpServletRequest request, Model model) {
		Map<String, ?> flashAttributes = RequestContextUtils.getInputFlashMap(request);

		if(flashAttributes != null) {
			for (Map.Entry<String, ?> keyVal : flashAttributes.entrySet()) {
				model.addAttribute(keyVal.getKey(), keyVal.getValue());
			}
		}

	}
}
