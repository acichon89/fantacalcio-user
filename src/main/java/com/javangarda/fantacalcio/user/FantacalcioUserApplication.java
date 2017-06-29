package com.javangarda.fantacalcio.user;

import com.javangarda.fantacalcio.user.application.gateway.QueryFacade;
import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.gateway.impl.SimpleQueryFacade;
import com.javangarda.fantacalcio.user.application.internal.AccessTokenGenerator;
import com.javangarda.fantacalcio.user.application.internal.UserDTOMapper;
import com.javangarda.fantacalcio.user.application.internal.UserFactory;
import com.javangarda.fantacalcio.user.application.internal.UserService;
import com.javangarda.fantacalcio.user.application.internal.impl.SimpleUserDTOMapper;
import com.javangarda.fantacalcio.user.application.internal.impl.SimpleUserFactory;
import com.javangarda.fantacalcio.user.application.internal.impl.TransactionalUserService;
import com.javangarda.fantacalcio.user.application.internal.impl.UniqueAccessTokenGenerator;
import com.javangarda.fantacalcio.user.application.internal.saga.CommandHandler;
import com.javangarda.fantacalcio.user.application.internal.saga.UserEventPublisher;
import com.javangarda.fantacalcio.user.application.internal.saga.impl.EventDrivenCommandHandler;
import com.javangarda.fantacalcio.user.application.internal.storage.UserRepository;
import com.javangarda.fantacalcio.user.infrastructure.port.adapter.messaging.Events;
import com.javangarda.fantacalcio.user.infrastructure.port.adapter.messaging.ExternalMessagingUserEventPublisher;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableEurekaClient
@RestController
@EnableBinding(Events.class)
@EnableIntegration
@IntegrationComponentScan(basePackages={"com.javangarda.fantacalcio.user"})
public class FantacalcioUserApplication implements AsyncConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(FantacalcioUserApplication.class, args);
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
	QueryFacade queryFacade(UserRepository userRepository, UserDTOMapper userDTOMapper) {
		return new SimpleQueryFacade(userRepository, userDTOMapper);
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
	public MessageChannel registerCommandChannel(){
		return new PublishSubscribeChannel(getAsyncExecutor());
	}

	@Bean
	public MessageChannel confirmUserCommandChannel(){
		return new PublishSubscribeChannel(getAsyncExecutor());
	}

	@Bean
	public MessageChannel changeEmailCommandChannel(){
		return new PublishSubscribeChannel(getAsyncExecutor());
	}

}
