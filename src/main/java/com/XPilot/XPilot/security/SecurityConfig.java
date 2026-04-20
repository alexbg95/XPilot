package com.XPilot.XPilot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        return new DaoAuthenticationProvider(passwordEncoder()) {{
            setUserDetailsService(userDetailsService);
        }};
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(authenticationProvider()));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login-view", "/register", "/error").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/media/**",
                                 "/firebase-messaging-sw.js").permitAll()
                .requestMatchers("/api/usuario/register").permitAll()
                .requestMatchers("/api/notificaciones/**").authenticated()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/guardar-fcm-token").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/usuario/**").hasAnyRole("USER", "ADMIN")
                // ✅ artista/** es público — cualquiera puede ver el perfil
                .requestMatchers("/artista/**").permitAll()
                // Contratar y galería requieren login
                .requestMatchers("/contratar/**", "/gallery", "/mis-contratos").authenticated()
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login-view")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    response.sendRedirect(isAdmin ? "/admin" : "/gallery");
                })
                .failureUrl("/login-view?error=true")
                .permitAll()
            )

            .sessionManagement(session -> session
                .invalidSessionUrl("/login-view?session=expired")
                .maximumSessions(1)
                .expiredUrl("/login-view?session=expired")
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            .exceptionHandling(ex -> ex
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/login-view?error=403");
                })
            )

            .authenticationProvider(authenticationProvider());

        return http.build();
    }
}
