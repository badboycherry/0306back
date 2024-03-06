package com.eco.eco_Api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.eco.eco_Api.DTO.CustomUserDetails;

@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserService userService;

    @Autowired
    public LoginSuccessListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        CustomUserDetails userDetails = (CustomUserDetails) event.getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        userService.loginUser(username);
    }
}
