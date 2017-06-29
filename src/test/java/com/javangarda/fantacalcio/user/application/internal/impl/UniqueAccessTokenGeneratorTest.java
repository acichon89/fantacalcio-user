package com.javangarda.fantacalcio.user.application.internal.impl;

import com.javangarda.fantacalcio.user.application.internal.storage.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class UniqueAccessTokenGeneratorTest {

    private UserRepository userRepository;

    private UniqueAccessTokenGenerator uniqueAccessTokenGenerator;

    @Before
    public void init(){
        userRepository = mock(UserRepository.class);
        uniqueAccessTokenGenerator = new UniqueAccessTokenGenerator(userRepository);
    }

    @Test
    public void shouldGenerateRandomConfirmEmailTokenAsLongAsIsNotUnique() {
        //given:
        when(userRepository.countUserWithConfirmEmailToken(anyString())).thenReturn(1).thenReturn(1).thenReturn(0);
        //when:
        String token = uniqueAccessTokenGenerator.createConfirmEmailToken();
        Assert.assertNotNull("ccc", token);
        verify(userRepository, times(3)).countUserWithConfirmEmailToken(anyString());
    }

    @Test
    public void shouldGenerateRandomResetPasswordTokenAsLongAsIsNotUnique() {
        //given:
        when(userRepository.countUserWithResetPasswordToken(anyString())).thenReturn(1).thenReturn(1).thenReturn(0);
        //when:
        String token = uniqueAccessTokenGenerator.createResetPasswordToken();
        Assert.assertNotNull("ccc", token);
        verify(userRepository, times(3)).countUserWithResetPasswordToken(anyString());
    }
}