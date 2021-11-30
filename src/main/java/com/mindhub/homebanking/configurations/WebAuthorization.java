package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/web/index.html","/web/img/**","/web/js/**","/web/styles/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers("/web/admin.html").hasAuthority("ADMIN")
                .antMatchers("/web/**","/api/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/**","/api/transactions","/api/loans","/api/payments","/api/create/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/create/loans").hasAuthority("ADMIN")
                .antMatchers("/api/***","/h2-console","/web/admin.html").hasAuthority("ADMIN");
        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");
        //desactivar la comprobación de tokens CSRF
        http.csrf().disable();
        //deshabilitar frameOptions para que se pueda acceder a la consola h2
        //httpSecurity.headers().frameOptions().disable();
        http.headers().frameOptions().disable();
        //si el usuario no está autenticado, simplemente envíe una respuesta de error de autenticación
        http.exceptionHandling().authenticationEntryPoint(((request, response, authException) -> {
          if(request.getRequestURI().contains("/web")){
              response.sendRedirect("/web/index.html");
          }
        }));
        //http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        //si el inicio de sesión es exitoso, simplemente borre las banderas que solicitan autenticación
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        //si el inicio de sesión falla, simplemente envíe una respuesta de falla de autenticación
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        //si el cierre de sesión es exitoso, simplemente envíe una respuesta exitosa
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
