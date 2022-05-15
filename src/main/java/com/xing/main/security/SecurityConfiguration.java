package com.xing.main.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] SWAGGER_WHITE_LIST = new String[]{
            "/v2/api-docs",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**"};

    @Autowired
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
        return new CustomBasicAuthenticationEntryPoint();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    	auth.inMemoryAuthentication().passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance()).withUser("user1").password("secret1")
//		.roles("USER").and().withUser("admin1").password("secret1")
//		.roles("USER", "ADMIN");
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
//        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
//        auth.inMemoryAuthentication().withUser("user1").password("secret1").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
//                .antMatchers(SWAGGER_WHITE_LIST).permitAll()
                .antMatchers("/api/user/create").permitAll()
                .antMatchers("/api/user/all").hasAuthority("USER")
                .antMatchers("/api/user/me").hasAuthority("USER")
                .antMatchers("/api/kingdom/create").hasAuthority("USER")
                .antMatchers("/api/kingdom/me").hasAuthority("USER")
                .antMatchers("/api/kingdom/me/action").hasAuthority("USER")
                .antMatchers("/api/kingdom/me/turn").hasAuthority("USER")
                .antMatchers("/**").hasAuthority("ADMIN")
//                .antMatchers("/api/employee/simple").hasRole("USER")
//                .antMatchers("/api/system/**").hasAuthority("ADMIN") // TODO allow this line
                
//                .antMatchers("/api/employee/simple").hasRole("USER")
//                .antMatchers("/login").permitAll()
//                .antMatchers("/api/user/all2").hasRole("USER")
//                .antMatchers("/api/user/all3").permitAll()
//                .antMatchers("/view/all").permitAll()
//                .antMatchers("/view/register").permitAll()
//                .anyRequest().authenticated()
                
                .and().httpBasic()/*.realmName("MY_TEST_REALM")*/.authenticationEntryPoint(getBasicAuthEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
//                .headers()
//                .frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

}
