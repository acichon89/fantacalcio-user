package com.javangarda.fantacalcio.user.application.internal.impl;

import com.javangarda.fantacalcio.user.application.internal.AccessTokenGenerator;
import com.javangarda.fantacalcio.user.application.storage.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@AllArgsConstructor
public class UniqueAccessTokenGenerator implements AccessTokenGenerator {

	private UserRepository userRepository;

	@Override
	public String createConfirmEmailToken() {
		String token;
		do {
			token = RandomStringUtils.randomAlphanumeric(25);
		} while (userRepository.countUserWithConfirmEmailToken(token) > 0);
		return token;
	}

	@Override
	public String createResetPasswordToken() {
		String token;
		do {
			token = RandomStringUtils.randomAlphanumeric(25);
		} while (userRepository.countUserWithResetPasswordToken(token) > 0);
		return token;
	}

}
