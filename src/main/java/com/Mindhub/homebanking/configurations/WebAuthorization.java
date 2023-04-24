package com.Mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/rest/**").denyAll()
                .antMatchers("/web/style/**", "/h2-console/**", "/web/js/**","/web/img/**","/web/index.html").permitAll()
                .antMatchers(HttpMethod.GET, "/api/logout", "/web/pages/accounts.html").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients", "/api/login").permitAll()

//                .antMatchers(HttpMethod.GET, "/api/accounts/").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/accounts").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/clients/").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/clients").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/transactions/").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/transactions").hasAuthority("ADMIN")

                .antMatchers("/web/pages/accounts.html", "/web/pages/account.html", "/web/pages/cards.html").hasAuthority("CLIENT");

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");
//                .defaultSuccessUrl("/web/pages/accounts.html", true);
        http.logout()
                .logoutUrl("/api/logout")
//                .logoutSuccessUrl("/web/index.html")
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

