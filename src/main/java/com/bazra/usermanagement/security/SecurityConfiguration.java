package com.bazra.usermanagement.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bazra.usermanagement.model.UserInfoService;
import com.bazra.usermanagement.util.JwtFilter;

/**
 * Security Configurations
 * 
 * @author Bemnet
 * @version 4/2022
 *
 */

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private EntrypointAuth entrypointAuth;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userInfoService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    
    
    
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(entrypointAuth).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers("/").permitAll().antMatchers("/api/auth/**", "/api/user/exist").permitAll()
                .antMatchers("/api")
                .permitAll().antMatchers("/api/signin/user")
                .permitAll().antMatchers("/api/signin/agent")
                .permitAll().antMatchers("/api/signin/merchant")
                .permitAll().antMatchers("/api/signup/user")
                .permitAll().antMatchers("/api/signup/agent")
                .permitAll().antMatchers("/api/signup/merchant")
                .permitAll().antMatchers("/api/users/update").permitAll().antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll().antMatchers("**/swagger-resources/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui/index.html").permitAll()
                .antMatchers("/api/accounts/page-One").permitAll()
                .antMatchers("/api/accounts/page-Two").permitAll()
                .antMatchers("/api/accounts/page-Three").permitAll()
//                .antMatchers("/api/account/withdraw").hasRole("AGENT")
                .anyRequest().authenticated();
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    }

}
