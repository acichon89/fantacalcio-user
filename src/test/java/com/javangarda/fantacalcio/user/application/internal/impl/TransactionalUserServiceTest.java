package com.javangarda.fantacalcio.user.application.internal.impl;

import com.javangarda.fantacalcio.user.application.data.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.data.dto.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.UserFactory;
import com.javangarda.fantacalcio.user.application.storage.User;
import com.javangarda.fantacalcio.user.application.storage.UserRepository;
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

    private TransactionalUserService transactionalUserService;

    @Before
    public void init(){
        userFactory = mock(UserFactory.class);
        userRepository = mock(UserRepository.class);
        transactionalUserService = new TransactionalUserService(userFactory, userRepository);
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
    public void should_return_persisted_mapped_user() {
        //given:
        User user = new User("ccc");
        user.register("Mickey Mouse", "mickey@disney.com", "4444444", Locale.ENGLISH);
        user.confirmEmail();
        when(userRepository.findByConfirmEmailTokenAndEmail("4444444", "mickey@disney.com")).thenReturn(Optional.of(user));

        //when:
        Optional<UserDTO> dto = transactionalUserService.getByConfirmationTokenAndEmail("4444444", "mickey@disney.com");

        //then:
        assertThat(dto).isPresent();
        assertThat(dto.get().getFullName()).isEqualTo("Mickey Mouse");
        assertThat(dto.get().getConfirmedEmail()).isEqualTo("mickey@disney.com");
    }
}