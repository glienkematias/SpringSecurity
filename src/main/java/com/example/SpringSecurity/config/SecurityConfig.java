package com.example.SpringSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     *
     * El build retorna el SecurityFilterChain
     * CSRF Cross-Site Request Forgery deprecada, se inhabilita cuando no hay formularios
     * authorizeHttpRequests que url estan protegidas
     */
    // Configuration 1
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        return  httpSecurity
//                .authorizeHttpRequests()
//                    .requestMatchers("/v1/index2").permitAll()
//                    .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll()
//                .and()
//                //.httpBasic()
//                //.and()
//                .build();
//
//        //.csrf().disable()
//    }

    //Configuration 2
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/v1/index2").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin()
                    .successHandler(successHandler()) //URL a donde se va a redirigir despues de iniciar sesion
                    .permitAll()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .invalidSessionUrl("/login")
                    .maximumSessions(1)
                    .expiredUrl("/login")
                    .sessionRegistry(sessionRegistry())
                .and()
                .sessionFixation()
                   .migrateSession()
                .and()
                .httpBasic()
                .and()
                .build();
    }
    // SessionCreationPolicy
    //ALWAYS crea session cuando no exista una, ya existe la reutiliza
    //IS_REQUIRED crea sesion solo si es necesario, ya existe la reutiliza. Es mas extricto
    //NEVER no crea sesion, si ya existe la utiliza
    //STATELESS no crea sesion, si ya existe NO la utiliza. trabaja de forma independiente

    //sessionFixation
    //migrateSession
    //newSession Crea sesion nueva
    //none Inhabilita la seguridad
    //

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler successHandler(){
        return ((request, response, authentication) -> {
            response.sendRedirect("/v1/session");
        });
    }

}
