package com.codeup.iknowaspot.config;

import com.codeup.iknowaspot.services.UserDetailsLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsLoader usersLoader;

    public SecurityConfiguration(UserDetailsLoader usersLoader) {
        this.usersLoader = usersLoader;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usersLoader) // How to find users by their username
                .passwordEncoder(passwordEncoder()) // How to encode and verify passwords
        ;
    }
//}
//    @Override
//    protected void configure(HttpSecurity security, AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .userDetailsService(usersLoader) // How to find users by their username
//                .passwordEncoder(passwordEncoder()) // How to encode and verify passwords
//                .and()
//        security.csrf()
//                .disable()
//                .httpBasic()
//                .disable()
//                .authorizeRequests()
//                .anyRequest()
//                .permitAll();
//    }
//}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /* Login configuration */
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home") // user's home page, it can be any URL
                .permitAll() // Anyone can go to the login page
                /* Logout configuration */
                .and()
                .logout()
                .logoutSuccessUrl("/home") // append a query string value
                /* Pages that can be viewed without having to log in */
                .and()
                /* Pages that require authentication */
                .authorizeRequests()
                .antMatchers(
                        "/spots/create", // only authenticated users can create ads
                        "/spots/{id}/edit", // only authenticated users can edit ads
                        "/profile", //only authenticated users can view profile
                        "/profile/edit" //only authenticated users can edit profile
                )
                .authenticated()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/home", "/spots", "/about", "/register") // anyone can see the home and the ads pages
                .permitAll()

        ;
    }
}