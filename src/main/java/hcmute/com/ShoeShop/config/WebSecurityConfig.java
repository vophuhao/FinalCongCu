package hcmute.com.ShoeShop.config;

import hcmute.com.ShoeShop.component.CustomAuthenticationProvider;
import hcmute.com.ShoeShop.component.CustomAuthenticationSuccessHandler;
import hcmute.com.ShoeShop.services.imp.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
        private final String[] PUBLIC_ENDPOINT = {"/", "/login", "/register"};
        private final String[] PUBLIC_CSS = {"/assets/**", "/css/**", "/fonts/**", "/img/**", "/js/**", "/lib/**",
                "/style.css"};
        @Autowired
        CustomAuthenticationSuccessHandler successHandler;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity.authorizeHttpRequests(request -> request
                                .requestMatchers("/manager/**").hasRole("manager")
                                .requestMatchers("/shipper/**").hasRole("shipper")
                                .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINT).permitAll()
                                .requestMatchers(PUBLIC_CSS).permitAll() // Cho phép truy cập tài nguyên tĩnh
                                .anyRequest().permitAll())
                        //config cho trang login
                        .formLogin(formLogin ->
                                formLogin.loginPage("/login")
                                        .successHandler(successHandler)
                                        .failureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error=true"))
                                        .permitAll()
                        )
                        //config cho trang logout
                        .logout(logout ->
                                logout.logoutUrl("/logout").permitAll()
                        );
                        //config cho remember me 1 day
                //cai nay tu bat nen phai tat
                httpSecurity.csrf(AbstractHttpConfigurer::disable);
                return httpSecurity.build();
        }


        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService() {
                return new CustomUserDetailService();
        }

}
