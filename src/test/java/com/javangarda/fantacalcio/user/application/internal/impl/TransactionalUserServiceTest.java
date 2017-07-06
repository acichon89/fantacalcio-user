package com.javangarda.fantacalcio.user.application.internal.impl;

import com.javangarda.fantacalcio.user.application.gateway.command.ChangeEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.AccessTokenGenerator;
import com.javangarda.fantacalcio.user.application.internal.UserDTOMapper;
import com.javangarda.fantacalcio.user.application.internal.UserFactory;
import com.javangarda.fantacalcio.user.application.internal.storage.User;
import com.javangarda.fantacalcio.user.application.internal.storage.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;


public class TransactionalUserServiceTest {

    private UserFactory userFactory;
    private UserRepository userRepository;
    private AccessTokenGenerator accessTokenGenerator;
    private UserDTOMapper userDTOMapper;

    private TransactionalUserService transactionalUserService;

    @Before
    public void init(){
        userFactory = mock(UserFactory.class);
        userRepository = mock(UserRepository.class);
        accessTokenGenerator = mock(AccessTokenGenerator.class);
        userDTOMapper = new SimpleUserDTOMapper();
        transactionalUserService = new TransactionalUserService(userFactory, userRepository, accessTokenGenerator, userDTOMapper);
    }

    @Test
    public void should_store_user_from_factory() {
        //given:
        RegisterUserCommand registerUserCommand = RegisterUserCommand.of("john@doe.com", "John Doe");
        User user = new User("123");
        user.setFullName("Johnny");
        when(userFactory.create(registerUserCommand)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        //when:
        UserDTO userDTO = transactionalUserService.saveUser(registerUserCommand);

        //then:
        assertThat(userDTO.getId()).isEqualTo("123");
        assertThat(userDTO.getFullName()).isEqualTo("Johnny");
        ArgumentCaptor<User> userAC = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userAC.capture());
        assertThat(userAC.getValue().getId()).isEqualTo("123");
    }

    @Test
    public void should_assing_email_while_confirm() {
        //given:
        User user = new User("123");
        user.register("Johnny Bravo", "johnny@bravo.com", "rrr", Locale.ENGLISH);
        when(userRepository.findByTmpEmail("johnny@bravo.com")).thenReturn(Optional.of(user));
        //when:
        transactionalUserService.confirmUserEmail("johnny@bravo.com");
        //then:
        verify(userRepository).findByTmpEmail("johnny@bravo.com");
        assertThat(user.getEmail()).isEqualTo("johnny@bravo.com");
    }

    @Test
    public void should_store_tmp_email() {
        //given:
        User user = new User("123");
        user.register("Johnny Bravo", "johnny@bravo.com", "rrr", Locale.ENGLISH);
        user.confirmEmail();
        when(userRepository.findByEmail("johnny@bravo.com")).thenReturn(Optional.of(user));
        when(accessTokenGenerator.createConfirmEmailToken()).thenReturn("abb-aa");
        //when:
        transactionalUserService.storeTmpEmail(ChangeEmailCommand.of("johnny@bravo.com", "johndoe@example.com"));
        //then:
        verify(userRepository).findByEmail("johnny@bravo.com");
        assertThat(user.getEmail()).isEqualTo("johnny@bravo.com");
        assertThat(user.getTmpEmail()).isEqualTo("johndoe@example.com");
        assertThat(user.getConfirmEmailToken()).isEqualTo("abb-aa");
    }
}