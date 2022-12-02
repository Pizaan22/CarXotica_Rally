package ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http
                .authorizeHttpRequests((authz) -> authz
                        .antMatchers("/").permitAll()
                        .antMatchers("/Home").permitAll()
                        .antMatchers("/Add").hasAnyRole("GUEST", "VENDOR")
                        .antMatchers("/View").hasAnyRole("GUEST", "VENDOR")
                        .antMatchers("/Edit").hasRole("VENDOR")
                        .antMatchers("/Delete").hasRole("VENDOR")
                        .antMatchers("/Statistics").hasRole("VENDOR")
                        .antMatchers("/AgePlot").hasRole("VENDOR")
                        .antMatchers("/GenderPlot").hasRole("VENDOR")
                        .antMatchers("/PriceRange").hasRole("VENDOR")
                        .antMatchers("/SignUp").permitAll()
                        .antMatchers("/images/**").permitAll()
                        .antMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin()
                .loginPage("/Login")
                .permitAll()
                .and()
                .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/Home")
                .permitAll();

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder)
                .and()
                .build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }
}
