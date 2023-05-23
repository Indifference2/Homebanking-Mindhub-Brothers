package com.Mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/web/style/**", "/web/js/**","/web/img/**","/web/index.html","/h2-console/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login","/api/clients").permitAll()
                /* Controllers request*/
                .antMatchers(HttpMethod.GET, "/api/accounts/{id}","/api/clients/current/accounts","/api/clients/current",
                        "/api/clients/current/accounts","/api/clients/current/cards", "/api/accounts/clients/name",
                        "/api/loans", "/api/accounts/{id}/transactions", "/api/transactions/date", "/api/accounts/{id}/transactions/dateBetween",  "/api/clients/current/rol").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers(HttpMethod.POST, "/api/logout", "/api/clients/current/accounts", "/api/clients/current/cards", "/api/transactions","/api/loans", "/api/accounts/{id}/transactions/dateBetween/pdf").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/clients/current/cards", "/api/clients/current/accounts").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/manager/loans").hasAuthority("ADMIN")
                /* Html request*/
                .antMatchers(HttpMethod.GET,"/web/pages/accounts.html","/web/pages/account.html", "/web/pages/cards.html", "/web/pages/create-cards.html", "/web/pages/loan-application.html", "/web/pages/transfers.html").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.GET,"/web/pages/**").hasAuthority("ADMIN")


                .anyRequest().denyAll();

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");
        http.logout()
                .logoutUrl("/api/logout")
                .deleteCookies("JSESSIONID");

        http.csrf().disable();
        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

    }

}

