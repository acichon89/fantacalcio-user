package com.javangarda.fantacalcio.user.application.internal.impl;

import com.javangarda.fantacalcio.user.application.data.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.internal.AccessTokenGenerator;
import com.javangarda.fantacalcio.user.application.storage.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class SimpleUserFactoryTest {

    private AccessTokenGenerator accessTokenGenerator;
    private Locale locale;

    private SimpleUserFactory simpleUserFactory;

    @Before
    public void init(){
        accessTokenGenerator = mock(AccessTokenGenerator.class);
        locale = new Locale("xy", "AB");

        simpleUserFactory = spy(new SimpleUserFactory(accessTokenGenerator, locale));
    }

    @Test
    public void shouldCreateUser() {
        //given:
        when(accessTokenGenerator.createConfirmEmailToken()).thenReturn("123456");
        when(simpleUserFactory.generateId()).thenReturn("abba-xyz");
        RegisterUserCommand registerUserCommand = RegisterUserCommand.of("john@doe.com", "John Doe");
        //when:
        User user = simpleUserFactory.create(registerUserCommand);
        //then:
        assertThat(user.getConfirmEmailToken()).isEqualTo("123456");
        assertThat(user.getId()).isEqualTo("abba-xyz");
        assertThat(user.getEmailLocale().getLanguage()).isEqualTo("xy");
        assertThat(user.getEmailLocale().getCountry()).isEqualTo("AB");
    }
}