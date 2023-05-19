package com.ftn.socialNetwork.security;

import com.ftn.socialNetwork.service.implementation.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Autowired
    private TokenUtils tokenUtils;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
         authProvider.setUserDetailsService(userDetailsService());
         authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
       http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
        http.authorizeRequests()

                .antMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/logout").permitAll()

                .antMatchers(HttpMethod.POST, "/api/groups/create").permitAll()
                .antMatchers(HttpMethod.POST, "/api/groups/update/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/groups/delete/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/groups/find/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/groups/all/{id}").permitAll()

                .antMatchers(HttpMethod.POST, "/api/posts/create").permitAll()
                .antMatchers(HttpMethod.POST, "/api/posts/update/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/posts/delete/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/posts/find/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/posts/all/{id}").permitAll()
                .anyRequest().authenticated().and()
                 .cors().and()

                 .addFilterBefore(new AuthenticationTokenFilter(userDetailsService(), tokenUtils), BasicAuthenticationFilter.class);

     http.csrf().disable();


        http.authenticationProvider(authenticationProvider());

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
         return (web) -> web.ignoring().antMatchers(HttpMethod.POST, "/api/users/login")
                 .antMatchers(HttpMethod.POST, "/api/users/signup")
                .antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico",
                        "/**/*.html", "/**/*.css", "/**/*.js");

    }
}
