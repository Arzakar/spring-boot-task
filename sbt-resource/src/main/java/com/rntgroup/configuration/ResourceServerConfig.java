package com.rntgroup.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    @Profile("default")
    WebSecurityConfigurerAdapter def() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests().anyRequest().permitAll();
                http.csrf().ignoringAntMatchers("/h2-console/**");
                http.headers().frameOptions().sameOrigin();
            }
        };
    }

    @Bean
    @Profile("local")
    WebSecurityConfigurerAdapter local() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests().anyRequest().permitAll();
                http.csrf().ignoringAntMatchers("/h2-console/**");
                http.headers().frameOptions().sameOrigin();
            }
        };
    }

    @Bean
    @Profile("!local")
    WebSecurityConfigurerAdapter basic() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.cors().and().csrf().disable()
                        .authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/celestial-bodies").permitAll()
                        .antMatchers(HttpMethod.POST, "/celestial-bodies").access("hasAuthority('SCOPE_write')")
                        .antMatchers(HttpMethod.GET, "/celestial-bodies/**").access("hasAuthority('SCOPE_read')")
                        .antMatchers(HttpMethod.PATCH, "/celestial-bodies").access("hasAuthority('SCOPE_write')")
                        .antMatchers(HttpMethod.DELETE, "/celestial-bodies/**").access("hasAuthority('SCOPE_write')")
                        .and()
                        .oauth2ResourceServer()
                        .jwt();
            }
        };
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/celestial-bodies").permitAll()
//                .antMatchers(HttpMethod.POST, "/celestial-bodies").access("hasAuthority('SCOPE_write')")
//                .antMatchers(HttpMethod.GET, "/celestial-bodies/**").access("hasAuthority('SCOPE_read')")
//                .antMatchers(HttpMethod.PATCH, "/celestial-bodies").access("hasAuthority('SCOPE_write')")
//                .antMatchers(HttpMethod.DELETE, "/celestial-bodies/**").access("hasAuthority('SCOPE_write')")
//                .and()
//                .oauth2ResourceServer()
//                .jwt();
//    }
//
//    private void localProfile(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/**").permitAll().anyRequest().anonymous();
//    }

}
