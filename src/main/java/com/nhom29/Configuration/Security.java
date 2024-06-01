package com.nhom29.Configuration;

import com.nhom29.Service.Oauth2.security.CustomOAuth2UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class Security {
    @Autowired
    private CustomOAuth2UserDetailService customOAuth2UserDetailService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf( c -> c.disable())
                .cors( c->c.disable())
                .authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/login", "/email/**", "/xacnhan/**", "/matkhau/**", "/xuli/**", "/taotaikhoan", "/test").permitAll()
                        .requestMatchers("/home", "/question/**", "/tag/**").hasAuthority("USER")
                        .anyRequest().authenticated()
//                        .anyRequest().permitAll()
                )
                .formLogin(
                        f -> f.loginPage("/login")
                                .loginProcessingUrl("/sign-in")
                                .defaultSuccessUrl("/prelogin", true)
                )
                .logout(
                        l -> l.invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .clearAuthentication(true)
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/login")
                )
                .oauth2Login(
                        o -> o.loginPage("/login")
                                .defaultSuccessUrl("/prelogin", true)
                                .userInfoEndpoint( i -> i.userService(customOAuth2UserDetailService))
                )
                .exceptionHandling( ex -> ex.accessDeniedPage("/error"));
        return http.build();
    }
    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT username, password, active from tai_khoan where username = ?"
        );
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select tk.username, u.role from uy_quyen as u " +
                        "join tai_khoan_thong_tin as tktt on u.id = tktt.uy_quyen_id " +
                        "join tai_khoan as tk on tk.username = tktt.tai_khoan_id " +
                        "join thong_tin as tt on tt.tai_khoan_thong_tin = tktt.id " +
                        "where tt.provider_id = 'local' and tk.username = ?"
        );
        return jdbcUserDetailsManager;
    }
    @Bean
    public AuthenticationManager authenticationManager( JdbcUserDetailsManager jdbcUserDetailsManager){
        var authentication = new DaoAuthenticationProvider();
        authentication.setUserDetailsService(jdbcUserDetailsManager);
        authentication.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authentication);
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) ->
                web.ignoring()
                        .requestMatchers("/js/**", "/css/**");
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
