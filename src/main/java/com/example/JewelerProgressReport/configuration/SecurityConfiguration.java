package com.example.JewelerProgressReport.configuration;


import com.example.JewelerProgressReport.security.token.jwt.JwtFilter;
import com.example.JewelerProgressReport.users.enums.roles.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeHttpRequests(
                        request -> {

                            request.requestMatchers(HttpMethod.POST,
                                    "/auth/telegram-auth",
                                    "/auth/jwt-refresh",
                                    "/auth/valid-token"
                            ).permitAll();

                            request.requestMatchers(
                                    "/shop",
                                    "/shop/all-short",
                                    "/shop/{shopId}/request-for-adding",
                                    "/shop/request-for-adding/cancel",
                                    "/user/guest",
                                    "/user"
                            ).authenticated();

                            request.requestMatchers( // super user
                                    "/report/{reportId}/approve",
                                    "/report/{reportId}/cancel",
                                    "/report/all/moderation",
                                    "/shop/all/moderation",
                                    "/shop/{shopId}/moderation",
                                    "/shop/{shopId}/approve",
                                    "/shop/{shopId}/cancel",
                                    "/moderation/**"
                            ).hasRole(Role.ROLE_SUPER_USER.name());


                            request.requestMatchers(HttpMethod.POST,// super user
                                            "/report/{reportId}/approve",
                                            "/report/{reportId}/cancel",
                                            "/shop/{userId}",
                                            "/shop/{shopId}/jeweler-master/access",
                                            "/shop/{shopId}/edit/admin",
                                            "/shop/{shopId}/edit/shop-assistants",
                                            "/shop/{shopId}/edit/jeweler",
                                            "/shop/{userId}",
                                            "/user"
                                    )
                                    .hasRole(Role.ROLE_SUPER_USER.name());

                            request.requestMatchers(HttpMethod.PATCH,
                                            "/user/{userId}/edit/shops"
                                    )
                                    .hasRole(Role.ROLE_SUPER_USER.name());


                            request.requestMatchers( // jeweler
                                    "/report/create/{userId}",
                                    "/report/create/{userId}/consultation",
                                    "/document/loadCSV"
                            ).hasAnyRole(Role.ROLE_JEWELER.name(), Role.ROLE_SUPER_USER.name());


                            request.requestMatchers(HttpMethod.POST, //director
                                    "/shop/{shopId}/add-admin",
                                    "/shop/{shopId}/add-shop-assistants",
                                    "/shop/{shopId}/add-jeweler",
                                    "/shop//{shopId}/edit/admin"
                            ).hasAnyRole(Role.ROLE_DIRECTOR.name(), Role.ROLE_SUPER_USER.name());

                        }

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
