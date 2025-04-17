package com.startup.demenage.security;

import static com.startup.demenage.security.Constants.API_URL_PREFIX;
import static com.startup.demenage.security.Constants.AUTHORITY_PREFIX;
import static com.startup.demenage.security.Constants.REFRESH_URL;
import static com.startup.demenage.security.Constants.ROLE_CLAIM;
import static com.startup.demenage.security.Constants.SIGNUP_URL;
import static com.startup.demenage.security.Constants.TOKEN_URL;
import static org.springframework.security.config.Customizer.withDefaults;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SuppressWarnings("deprecation")
public class SecurityConfig {

    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000; // 15 min
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 jours

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // AuthorizationManager<HttpServletRequest> cusAuthenticationManager = new
        // CustomAuthorizationManager();
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
        // configuration.setAllowCredentials(true);
        // For CORS response headers
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private RSAPublicKey publicKey() throws Exception {
        return (RSAPublicKey) loadPublicKey("src/main/resources/keys/public.pem");
    }

    private static PrivateKey loadPrivateKey() throws Exception {
        // Lire le contenu du fichier public.pem
        String key = System.getProperty("PRIVATE_KEY_PEM");
        if (key == null) {
            Dotenv dotenv = Dotenv.configure().load();
            key = dotenv.get("PRIVATE_KEY_PEM");
        }

        // Supprimer les en-têtes et pieds de la clé
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        // Décoder la clé en Base64
        byte[] keyBytes = Base64.getDecoder().decode(key);

        // Construire une clé RSA publique
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey loadPublicKey(String filePath) throws Exception {
        // Lire le contenu du fichier public.pem
        String key = new String(Files.readAllBytes(Paths.get(filePath)));

        // Supprimer les en-têtes et pieds de la clé
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", ""); // Supprimer les espaces et sauts de ligne

        // Décoder la clé en Base64
        byte[] keyBytes = Base64.getDecoder().decode(key);

        // Construire une clé RSA publique
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static Map<String, String> generateTokens(String username) throws Exception {
        long now = System.currentTimeMillis();
        PrivateKey privateKey = loadPrivateKey();
        String accessToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRATION))
                .signWith(privateKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRATION))
                .signWith(privateKey)
                .compact();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }
}