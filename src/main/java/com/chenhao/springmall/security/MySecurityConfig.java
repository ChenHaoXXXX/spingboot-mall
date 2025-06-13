package com.chenhao.springmall.security;

import com.chenhao.springmall.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.chenhao.springmall.constant.RoleConstants.*;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class MySecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Autowired
    @Lazy
    private MyUserDetailsService myUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        //return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request
                        // 開放路由
                        .requestMatchers("/", "/login", "/register", "/api/login", "/api/register").permitAll()

                        
                        // 商品相關
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers("/products").permitAll()
                        // 新增商品頁面和API
                        .requestMatchers("/products/**").permitAll()
                       // .requestMatchers("/products/new").hasAnyRole(MERCHANT, ADMIN)
                        .requestMatchers( "/api/products/**").permitAll()
                        //.requestMatchers(HttpMethod.POST, "/api/products/**").hasAnyRole(MERCHANT, ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").permitAll()
                        //.requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyRole(MERCHANT, ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyRole(MERCHANT, ADMIN)
                        
                        // 管理功能
                        .requestMatchers("/members/**", "/admin/**").hasRole(ADMIN)
                        
                        // 其他請求
                        .anyRequest().authenticated()
                )
                .userDetailsService(myUserDetailsService)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(provider);
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
