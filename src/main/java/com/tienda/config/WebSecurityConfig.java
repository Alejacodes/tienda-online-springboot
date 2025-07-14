package com.tienda.config;

import com.tienda.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

        private final UserDetailsServiceImpl userDetailsService;

        public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authenticationProvider(authenticationProvider())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/login.html",
                                                                "/registro.html",
                                                                "/js/**",
                                                                "/css/**",
                                                                "/img/**")
                                                .permitAll() // Archivos públicos
                                                .requestMatchers(
                                                                "/bienvenida.html",
                                                                "/productos.html",
                                                                "/categorias.html")
                                                .authenticated() // Páginas protegidas
                                                .requestMatchers("/api/auth/**", "/api/usuarios", "/api/usuarios/me")
                                                .permitAll() // Registro y login por API
                                                .requestMatchers("/api/**").authenticated() // Todas las demás rutas de
                                                                                            // la API protegidas
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login.html")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/bienvenida.html", true) // ✅ Cambiado a
                                                                                             // bienvenida.html
                                                .failureUrl("/login.html?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login.html?logout")
                                                .permitAll());

                return http.build();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setUserDetailsService(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder());
                return provider;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}