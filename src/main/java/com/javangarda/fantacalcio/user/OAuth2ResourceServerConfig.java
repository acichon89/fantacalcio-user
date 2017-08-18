package com.javangarda.fantacalcio.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private ResourceServerProperties sso;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.
            sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
            authorizeRequests()
            .antMatchers( "/register", "/usersWithEmailToConfirm", "/usersWithForgottenPassword").permitAll()
            .anyRequest().authenticated();
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenService());
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Primary
    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl("http://fantacalcio-auth-server/oauth/check_token");
        tokenService.setClientId(sso.getClientId());
        tokenService.setClientSecret(sso.getClientSecret());
        tokenService.setRestTemplate(restTemplate());
        return tokenService;
    }


}