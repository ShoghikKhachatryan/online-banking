package com.example.onlinebankingsystem.config;

import com.example.onlinebankingsystem.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class we use to set user's id in session in the case of success
 */
@Component
public class UserIdSessionAttributeHandler implements AuthenticationSuccessHandler {

    private final HttpSession session;

    private final UserService userService;

    UserIdSessionAttributeHandler(HttpSession session, UserService userService) {
        this.session = session;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        session.setAttribute("userId", userService.findByUsername(authentication.getName()).getId());
        setRedirectUrl(request, response, authentication);

    }

    private void setRedirectUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/");
        handler.setAlwaysUseDefaultTargetUrl(true);
        handler.onAuthenticationSuccess(request, response, authentication);
    }
}
