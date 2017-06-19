package com.javangarda.fantacalcio.user;

import com.javangarda.fantacalcio.user.application.gateway.UserGateway;
import com.javangarda.fantacalcio.user.application.gateway.impl.EventDrivenUserGateway;
import com.javangarda.fantacalcio.user.application.internal.AccessTokenGenerator;
import com.javangarda.fantacalcio.user.application.internal.UserFactory;
import com.javangarda.fantacalcio.user.application.internal.UserService;
import com.javangarda.fantacalcio.user.application.internal.impl.SimpleUserFactory;
import com.javangarda.fantacalcio.user.application.internal.impl.TransactionalUserService;
import com.javangarda.fantacalcio.user.application.internal.impl.UniqueAccessTokenGenerator;
import com.javangarda.fantacalcio.user.application.saga.UserEventPublisher;
import com.javangarda.fantacalcio.user.application.storage.UserRepository;
import com.javangarda.fantacalcio.user.infrastructure.port.adapter.messaging.Events;
import com.javangarda.fantacalcio.user.infrastructure.port.adapter.messaging.ExternalMessagingUserEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@SpringBootApplication
@EnableEurekaClient
@RestController
@EnableBinding(Events.class)
public class FantacalcioUserApplication {

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
	UserService userService(UserRepository userRepository, UserFactory userFactory){
		return new TransactionalUserService(userFactory, userRepository);
	}

	@Bean
	UserGateway userGateway(UserService userService, UserEventPublisher userEventPublisher) {
		return new EventDrivenUserGateway(userService, userEventPublisher);
	}

}
