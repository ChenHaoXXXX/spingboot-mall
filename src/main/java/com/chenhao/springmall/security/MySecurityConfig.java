package com.chenhao.springmall.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.chenhao.springmall.constant.RoleConstants.*;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class MySecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        //return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/products", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .authorizeHttpRequests(request -> request
                        //創建帳號
                        .requestMatchers("/register").permitAll()
                        //操作帳號
                        .requestMatchers("/members/**").hasRole(ADMIN)
                        //查詢指定商品、商品清單
                        .requestMatchers(HttpMethod.GET,"/products/*","/products").hasAnyRole(NORMAL_MEMBER,MERCHANT,ADMIN)
                        //操作商品
                        .requestMatchers("/products/**").hasAnyRole(MERCHANT,ADMIN)
                        // 管理員專用
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().denyAll()
                )
                .build();

    }


//    @Bean
//    public InMemoryUserDetailsManager userDetailsManager() {
//        UserDetails userTest1 = User
//                .withUsername("test1")
//                .password("{noop}111")
//                .roles("ADMIN", "USER")
//                .build();
//
//        UserDetails userTest2 = User
//                .withUsername("test2")
//                .password("{bcrypt}$2a$12$iiZJ4Y7NpSlLf1slp5jOwOaNODGf0Dsb2kAMBw.MBwA8Q27.2Vnka")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(userTest1, userTest2);
//    }
}
