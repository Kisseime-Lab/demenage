package com.startup.demenage.security;

import static com.startup.demenage.security.Constants.API_URL_PREFIX;
import static com.startup.demenage.security.Constants.AUTHORITY_PREFIX;
import static com.startup.demenage.security.Constants.REFRESH_URL;
import static com.startup.demenage.security.Constants.ROLE_CLAIM;
import static com.startup.demenage.security.Constants.SIGNUP_URL;
import static com.startup.demenage.security.Constants.TOKEN_URL;
import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SuppressWarnings("deprecation")
public class SecurityConfig {

    @Value("${app.security.jwt.keystore-location}")
    private String keyStorePath;
    @Value("${app.security.jwt.keystore-password}")
    private String keyStorePassword;
    @Value("${app.security.jwt.key-alias}")
    private String keyAlias;
    @Value("${app.security.jwt.private-key-passphrase}")
    private String privateKeyPassphrase;

    private static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(API_URL_PREFIX))
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .cors(withDefaults())
                .authorizeHttpRequests(req -> req
                        .requestMatchers(new AntPathRequestMatcher(TOKEN_URL, HttpMethod.POST.name())).permitAll()
                        .requestMatchers(new AntPathRequestMatcher(TOKEN_URL, HttpMethod.DELETE.name())).permitAll()
                        .requestMatchers(new AntPathRequestMatcher(SIGNUP_URL, HttpMethod.POST.name())).permitAll()
                        .requestMatchers(new AntPathRequestMatcher(REFRESH_URL, HttpMethod.POST.name())).permitAll()
                        .requestMatchers(new AntPathRequestMatcher(API_URL_PREFIX, HttpMethod.GET.name())).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(getJwtAuthenticationConverter())))
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    private Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorityConverter = new JwtGrantedAuthoritiesConverter();
        authorityConverter.setAuthorityPrefix(AUTHORITY_PREFIX);
        authorityConverter.setAuthoritiesClaimName(ROLE_CLAIM);
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorityConverter);
        return converter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        // For CORS response headers
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public KeyStore keyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream resStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStorePath);
            if (resStream == null) {
                throw new IllegalArgumentException("Keystore file not found: " + keyStorePath);
            }

            try (InputStream stream = resStream) {
                keyStore.load(stream, keyStorePassword.toCharArray());
            }

            // Verify the keystore contains the expected alias
            if (!keyStore.containsAlias("jwt-sign-key")) {
                throw new IllegalStateException("Keystore does not contain alias 'jwt-key'");
            }

            // Verify the certificate exists
            Certificate cert = keyStore.getCertificate("jwt-sign-key");
            if (cert == null) {
                throw new IllegalStateException("No certificate found for alias 'jwt-key'");
            }
            return keyStore;
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            logger.error("Unable to load keystore: {}",
                    keyStorePath, e);
        }
        throw new IllegalArgumentException("Can't load keystore");
    }

    @Bean
    RSAPrivateKey loadPrivateKey(KeyStore keyStore) {
        try {
            Key key = keyStore.getKey(keyAlias, keyStorePassword.toCharArray());
            return (RSAPrivateKey) key;
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            logger.warn("key from Keystore: {}", keyStorePath, e);
        }
        throw new IllegalArgumentException("Can't load private key");
    }

    @Bean
    RSAPublicKey loadPublicKey(KeyStore keyStore) {
        try {
            Certificate certificate = keyStore.getCertificate(keyAlias);
            PublicKey publicKey = certificate.getPublicKey();
            return (RSAPublicKey) publicKey;
        } catch (KeyStoreException e) {
            logger.error("key from keyStore: {}", keyStorePath, e);

        }
        throw new IllegalArgumentException("Can't load public key");
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}