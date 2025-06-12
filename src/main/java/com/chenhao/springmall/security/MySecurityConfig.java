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
                        .requestMatchers("/", "/login", "/register", "/api/login","api/register").permitAll()
                        // 讓所有人都能查詢商品
                        .requestMatchers(HttpMethod.GET, "api/products/**","/products").permitAll()
                        //測試JWT
                        .requestMatchers("/api/hello").authenticated()
                        // 商品操作限商家與管理員
                        .requestMatchers("api/products/**").hasAnyRole(MERCHANT, ADMIN)

                        // 管理帳號、後台
                        .requestMatchers("/members/**").hasRole(ADMIN)
                        .requestMatchers("/admin/**").hasRole(ADMIN)
                        .anyRequest().denyAll()
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
