package com.startup.demenage.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import java.util.Map;


import static com.startup.demenage.security.Constants.ENCODER_ID;

@Configuration
public class AppConfig {

    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
    @Bean
    @Lazy
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders =
                Map.of(
                        ENCODER_ID, new BCryptPasswordEncoder(),
                        "pbkdf2", Pbkdf2PasswordEncoder.
                                defaultsForSpringSecurity_v5_8(),
                        "scrypt", SCryptPasswordEncoder
                                .defaultsForSpringSecurity_v5_8());
        return new DelegatingPasswordEncoder
                (ENCODER_ID, encoders);
    }
    @Bean
    @Lazy
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }
}
