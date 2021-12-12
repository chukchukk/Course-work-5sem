package com.ponomarev.coursework.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String redirectURL = "/notFound";

        if(principal.getAuthorities().contains(com.ponomarev.coursework.model.User.Role.USER)) {
            redirectURL = "/client";
        }
        else if(principal.getAuthorities().contains(com.ponomarev.coursework.model.User.Role.ADMIN)) {
            redirectURL = "/admin";
        }
        response.sendRedirect(redirectURL);
    }
}
