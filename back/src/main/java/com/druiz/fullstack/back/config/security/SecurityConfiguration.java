package com.druiz.fullstack.back.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;

/**
 * Clase de configuración de seguridad de la aplicación de Spring Webflux
 */
@Configuration
// Habilita la seguridad web para la aplicación
@EnableWebFluxSecurity
// Habilita la seguridad de los métodos para la aplicación
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    private final ReactiveUserDetailsService reactiveUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor que recibe las dependencias necesarias para la configuración de seguridad
     *
     * @param reactiveUserDetailsService servicio reactivo que gestiona la información de usuario y autenticación
     * @param passwordEncoder            encodificador de contraseñas
     */
    public SecurityConfiguration(ReactiveUserDetailsService reactiveUserDetailsService, PasswordEncoder passwordEncoder) {
        this.reactiveUserDetailsService = reactiveUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Bean que se encarga de la gestión de seguridad en la aplicación
     *
     * @param http objeto que permite la configuración de seguridad en la aplicación
     * @return objeto que implementa la cadena de filtros de seguridad
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                // Permite el acceso a la página de inicio de sesión sin autenticación
                .pathMatchers("/login").permitAll()
                // Requiere autenticación para todas las demás solicitudes
                .anyExchange().authenticated()
                .and()
                .formLogin()
                // URL de la página de inicio de sesión
                .loginPage("/login")
                .and()
                .addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public AuthenticationWebFilter webFilter() {
        AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/"));
        return authenticationFilter;
    }

    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }
}

