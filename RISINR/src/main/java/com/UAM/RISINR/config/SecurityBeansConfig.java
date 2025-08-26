package com.UAM.RISINR.config;

import com.UAM.RISINR.security.Cybersecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityBeansConfig {

    @Bean
    public Cybersecurity cybersecurity() throws Exception {
        Cybersecurity c = new Cybersecurity();
        c.generarLlaves(); 
        return c;
    }
}
