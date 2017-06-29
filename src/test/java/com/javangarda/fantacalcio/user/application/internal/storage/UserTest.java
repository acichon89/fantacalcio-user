package com.javangarda.fantacalcio.user.application.internal.storage;

import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

public class UserTest {

    @Test
    public void shouldClearTokenAfterRegister(){
        //given:
        User user = new User("123");
        user.register("John Doe", "john@doe.com", "token", Locale.ENGLISH);
        //when:
        user.confirmEmail();
        assertThat(user.getConfirmEmailToken()).isNull();
        assertThat(user.getTmpEmail()).isNull();
        assertThat(user.getEmail()).isEqualTo("john@doe.com");
    }

    @Test
    public void shouldHaveConfirmationToken(){
        //given:
        User user = new User("123");
        user.register("John Doe", "john@doe.com", "token", Locale.ENGLISH);
        //when:
        boolean hasToken = user.hasConfirmationEmailToken("token");
        //then:
        assertThat(hasToken).isTrue();
    }
}