package com.ponomarev.coursework;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;


@SpringBootApplication
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
public class Main {

    private final MessageSource ms;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    //TODO mapping for errors
    @PostConstruct
    public void dfg() {
        System.out.println(ms.getMessage("typeMismatch.createSavingAccountDTO.sum", null, LocaleContextHolder.getLocale()));
    }

}
