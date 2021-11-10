package com.ponomarev.coursework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleController {
    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }
}
