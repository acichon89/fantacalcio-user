package com.javangarda.fantacalcio.user.application.gateway.impl;

import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.UserDTOMapper;
import com.javangarda.fantacalcio.user.application.internal.impl.SimpleUserDTOMapper;
import com.javangarda.fantacalcio.user.application.internal.storage.User;
import com.javangarda.fantacalcio.user.application.internal.storage.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;


public class SimpleQueryFacadeTest {

    private UserRepository userRepository;
    private UserDTOMapper userDTOMapper;

    private SimpleQueryFacade simpleQueryFacade;

    @Before
    public void init(){
        userRepository = Mockito.mock(UserRepository.class);
        userDTOMapper = new SimpleUserDTOMapper();
        simpleQueryFacade = new SimpleQueryFacade(userRepository, userDTOMapper);
    }

    @Test
    public void should_map_what_repo_returns() {
        //given:
        User user = new User("xyz");
        user.register("Homer Simpson", "homer@simpsons.com", "xyzyxz", Locale.ENGLISH);
        Mockito.when(userRepository.findByConfirmEmailTokenAndEmail("xyzyxz", "homer@simpsons.com")).thenReturn(Optional.of(user));
        //when:
        Optional<UserDTO> userDTO = simpleQueryFacade.getByConfirmationTokenAndEmail("xyzyxz", "homer@simpsons.com");
        //then:
        assertTrue(userDTO.isPresent());
        assertEquals("Homer Simpson", userDTO.get().getFullName());
    }
}