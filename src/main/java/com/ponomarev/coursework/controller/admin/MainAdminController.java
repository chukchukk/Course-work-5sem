package com.ponomarev.coursework.controller.admin;

import com.ponomarev.coursework.dto.CardInfoDTO;
import com.ponomarev.coursework.dto.RegisterNewClientDTO;
import com.ponomarev.coursework.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class MainAdminController {
    private final AdminService adminService;

    @GetMapping
    public String indexPage() {
        return "admin/admin_page";
    }

    @GetMapping("/newCard")
    public String newCardPage(HttpServletRequest request, Model model) {
        return adminService.newCardPage(request, model);
    }

    //TODO Сделать лист клиентов, регистрация карт для них
    @GetMapping("/clientList")
    public String clientListPage(HttpServletRequest request, Model model) {
        return adminService.clientListPage(request, model);
    }

    @PostMapping("/registerNewClient")
    public String registerNewClient(@ModelAttribute @Valid RegisterNewClientDTO dto,
                                    BindingResult errors, RedirectAttributes redirectAttributes) {
        return adminService.registerNewClient(errors, dto, redirectAttributes);
    }

    @GetMapping("/createCardForUser/{id}")
    public String createCardForUserPage(HttpServletRequest request,
                                        Model model,
                                        @PathVariable Long id) {
        return adminService.createCardForUserPage(request, model, id);
    }

    @PostMapping("/createCardForUser/{id}")
    public String createCardForUser(@PathVariable Long id,
                                    @Valid @ModelAttribute CardInfoDTO cardInfoDTO,
                                    BindingResult errors,
                                    RedirectAttributes redirectAttributes) {
        return adminService.createCardForUser(id, cardInfoDTO, errors, redirectAttributes);
    }

}
