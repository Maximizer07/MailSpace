package com.maximus.mailspace;

import com.maximus.mailspace.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends
        WebSecurityConfigurerAdapter {

    private UserService userService;
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void setUserDetailsService(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws
            Exception {
        http    .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/sign-up/**","/sign-up","/login", "/auth", "/css/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin().loginPage("/auth")
                    .loginProcessingUrl("/login")
                    .usernameParameter("username")
                    .defaultSuccessUrl("/user_info",true)
                    .permitAll()
                .and()
                    .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                .and()
                    .logout()
                    .deleteCookies("dummyCookie")
                    .permitAll()
                    .logoutSuccessUrl("/");
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }


}
