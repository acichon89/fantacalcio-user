package com.javangarda.fantacalcio.user;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableFeignClients
public class FeignSecureConfig {

    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(){
        OAuth2FeignRequestInterceptor interceptor = new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
        interceptor.setAccessTokenProvider(new LoadBalancedClientCredentialsAccessTokenProvider(restTemplate()));
        return interceptor;
    }

    @Bean
    public OAuth2RestTemplate clientCredentialsRestTemplate() {
        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
    }
}
class LoadBalancedClientCredentialsAccessTokenProvider extends ClientCredentialsAccessTokenProvider {

    private RestTemplate restTemplate;

    public LoadBalancedClientCredentialsAccessTokenProvider(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    protected RestOperations getRestTemplate() {
        return this.restTemplate;
    }

    @Override
    protected ResponseExtractor<OAuth2AccessToken> getResponseExtractor() {
        return new HttpMessageConverterExtractor<OAuth2AccessToken>(OAuth2AccessToken.class, this.restTemplate.getMessageConverters());
    }
}