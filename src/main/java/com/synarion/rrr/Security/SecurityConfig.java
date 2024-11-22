package com.synarion.rrr.Security;


import com.synarion.rrr.Service.SubAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private final SubAdminService subAdminService;

    public SecurityConfig(@Lazy SubAdminService subAdminService){
        this.subAdminService = subAdminService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(subAdminService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login","/css/**", "/js/**","/img/**","/vendor/**").permitAll()
                        .anyRequest().authenticated()

                )
                .formLogin(form -> form
                        .loginPage("/rrr/admin/login")
                        .usernameParameter("userName")
                        .passwordParameter("password")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());;

        return http.build();
    }
}
