package uk.ac.ed.acp.cw2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//I had to make this config file to allow access to the H2 repo (It wasn't working before)
//This would change to be more secure in production but this is just for demonstrative purposes
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            //allow H2 console frames
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            //disable CSRF completely (for development)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    //allow literally everything
                    .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());
        return http.build();
    }
}
