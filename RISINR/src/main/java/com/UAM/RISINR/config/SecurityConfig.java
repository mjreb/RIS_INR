package com.UAM.RISINR.config;

import com.UAM.RISINR.security.Cybersecurity;
import com.UAM.RISINR.security.jwt.JwtAuthenticationFilter;
import com.UAM.RISINR.service.access.AccessService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * Reglas básicas:
 *  - /access/**, /RISFSM/** y archivos estáticos públicos quedan abiertos.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Filtro que valida el Bearer token en cada petición.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(Cybersecurity cybersecurity,
                                                           AccessService accessService) {
        return new JwtAuthenticationFilter(cybersecurity, accessService);
    }

    /**
     * X-Forwarded-For / X-Real-IP Spring las interpretará correctamente.
     */
    @Bean
    ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

    /**
     * Cadena principal de filtros/autoriza­ción.
     */
    @Bean
    public SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http,
                                           JwtAuthenticationFilter jwtFilter) throws Exception {
        http.csrf(csrf -> csrf.disable())
           .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
           .authorizeHttpRequests(auth -> auth
                .requestMatchers("/access/login", "/access/seleccionar-rol").permitAll()
                .requestMatchers("/login.html", "/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/RISFSM/**").permitAll()
                // /access/logout -> REQUIERE autenticación
                .anyRequest().authenticated()
           )
      .exceptionHandling(ex -> ex
          // No autenticado → 401
          .authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
          // Autenticado sin permiso → 403
          .accessDeniedHandler((req, res, e) -> res.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
      )
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
