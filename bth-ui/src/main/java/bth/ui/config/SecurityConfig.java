package bth.ui.config;

import bth.common.contract.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        return http
                .csrf(csrf -> {
                    csrf.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
                            .csrfTokenRepository(csrfTokenRepository)
                            .sessionAuthenticationStrategy(new CsrfAuthenticationStrategy(csrfTokenRepository));
                })
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/login").not().authenticated();
                    registry.requestMatchers("/post", "/subscriptions", "/chat/**", "/notification/token").authenticated();
                    registry.anyRequest().permitAll();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1))
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(new RegisterUserAuthSuccessHandler()))
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID"))
                .exceptionHandling(exception -> {
                    exception.accessDeniedPage("/posts");
                })
                .build();
    }

    private class RegisterUserAuthSuccessHandler implements AuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            if (authentication.getPrincipal() instanceof OAuth2AuthenticatedPrincipal principal) {
                userService.registerIfNotExist(
                        principal.getName(),
                        principal.getAttribute("name"),
                        principal.getAttribute("email")
                );
                response.sendRedirect("/posts");
                return;
            }
            throw new IllegalStateException("Invalid principal type : "
                    + authentication.getPrincipal().getClass().getName());
        }
    }
}
