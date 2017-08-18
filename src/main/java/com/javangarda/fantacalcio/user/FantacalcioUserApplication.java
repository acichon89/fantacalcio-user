package com.javangarda.fantacalcio.user;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.javangarda.fantacalcio.commons.authentication.CurrentUserResolver;
import com.javangarda.fantacalcio.commons.authentication.impl.SecurityContextCurrentUserResolver;
import com.javangarda.fantacalcio.user.application.gateway.CommandBus;
import com.javangarda.fantacalcio.user.application.gateway.QueryFacade;
import com.javangarda.fantacalcio.user.application.gateway.impl.SimpleQueryFacade;
import com.javangarda.fantacalcio.user.application.internal.*;
import com.javangarda.fantacalcio.user.application.internal.impl.SimpleUserDTOMapper;
import com.javangarda.fantacalcio.user.application.internal.impl.SimpleUserFactory;
import com.javangarda.fantacalcio.user.application.internal.impl.TransactionalUserService;
import com.javangarda.fantacalcio.user.application.internal.impl.UniqueAccessTokenGenerator;
import com.javangarda.fantacalcio.user.application.internal.saga.CommandHandler;
import com.javangarda.fantacalcio.user.application.internal.saga.UserEventPublisher;
import com.javangarda.fantacalcio.user.application.internal.saga.impl.EventDrivenCommandHandler;
import com.javangarda.fantacalcio.user.application.internal.storage.UserRepository;
import com.javangarda.fantacalcio.user.application.internal.storage.dataprojection.DataProjectionRepository;
import com.javangarda.fantacalcio.user.infrastructure.adapter.income.messaging.Events;
import com.javangarda.fantacalcio.user.infrastructure.adapter.income.messaging.ExternalMessagingAccountEventHandler;
import com.javangarda.fantacalcio.user.infrastructure.adapter.income.messaging.ExternalMessagingUserEventPublisher;
import com.javangarda.fantacalcio.user.infrastructure.adapter.outcome.persistence.JdbcDataProjectionRepository;
import feign.RequestInterceptor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableEurekaClient
@EnableBinding(Events.class)
@EnableIntegration
@IntegrationComponentScan(basePackages={"com.javangarda.fantacalcio.user"})
@EnableJpaAuditing(auditorAwareRef = "auditorInstantAware")
@EnableConfigurationProperties
public class FantacalcioUserApplication implements AsyncConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(FantacalcioUserApplication.class, args);
	}

	@Bean
	public AuditorAware<LocalDateTime> auditorInstantAware() {
		return () -> LocalDateTime.now();
	}

	@Bean
	public CurrentUserResolver currentUserResolver() {
		return new SecurityContextCurrentUserResolver();
	}

	@Bean
	public ExternalMessagingAccountEventHandler externalMessagingAccountEventHandler(CommandBus commandBus) {
		return new ExternalMessagingAccountEventHandler(commandBus);
	}

	@Bean
	UserEventPublisher userEventPublisher(Events events) {
		return new ExternalMessagingUserEventPublisher(events);
	}

	@Bean
	AccessTokenGenerator accessTokenGenerator(UserRepository userRepository) {
		return new UniqueAccessTokenGenerator(userRepository);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	@RefreshScope
	UserFactory userFactory(AccessTokenGenerator accessTokenGenerator,
							@Value("${fantacalcio.user.email.defaultLocale.language}") String defaultEmailLocaleLanguage,
							@Value("${fantacalcio.user.email.defaultLocale.region}") String defaultEmailLocaleRegion) {
		return new SimpleUserFactory(accessTokenGenerator, new Locale(defaultEmailLocaleLanguage, defaultEmailLocaleRegion));
	}

	@Bean
	UserService userService(UserRepository userRepository, UserFactory userFactory, AccessTokenGenerator accessTokenGenerator, UserDTOMapper userDTOMapper){
		return new TransactionalUserService(userFactory, userRepository, accessTokenGenerator, userDTOMapper);
	}

	@Bean
	QueryFacade queryFacade(DataProjectionRepository dataProjectionRepository) {
		return new SimpleQueryFacade(dataProjectionRepository);
	}

	@Bean
	CommandHandler commandHandler(UserService userService, UserEventPublisher userEventPublisher) {
		return new EventDrivenCommandHandler(userService, userEventPublisher);
	}

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(30);
		executor.setMaxPoolSize(30);
		executor.setQueueCapacity(30);
		executor.initialize();
		return executor;
	}

	@Bean
	UserDTOMapper userDTOMapper(){
		return new SimpleUserDTOMapper();
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SimpleAsyncUncaughtExceptionHandler();
	}

	@Bean
	public MessageChannel startResettingPasswordProcedureCommandChannel(){
		return new PublishSubscribeChannel(getAsyncExecutor());
	}


	@Bean
	public MessageChannel registerCommandChannel(){
		return new PublishSubscribeChannel(getAsyncExecutor());
	}

	@Bean
	public MessageChannel confirmUserCommandChannel(){
		return new PublishSubscribeChannel(getAsyncExecutor());
	}

	@Bean
	public MessageChannel confirmEmailCommandChannel(){
		return new PublishSubscribeChannel(getAsyncExecutor());
	}

	@Bean
	public MessageChannel startChangingEmailProcedureCommandChannel(){
		return new PublishSubscribeChannel(getAsyncExecutor());
	}

	@Bean
	public MessageChannel resetPasswordCommandChannel(){
		return new PublishSubscribeChannel(getAsyncExecutor());
	}

	@Bean
	public MessageChannel changePasswordCommandChannel(){
		return new PublishSubscribeChannel(getAsyncExecutor());
	}

	@Bean
	public DataProjectionRepository dataProjectionRepository(JdbcTemplate jdbcTemplate) {
		return new JdbcDataProjectionRepository(jdbcTemplate);
	}

	@Bean
	public ObjectMapper objectMapper(){
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}

}
