package com.javangarda.fantacalcio.user.application.internal.impl;

import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.UserDTOMapper;
import com.javangarda.fantacalcio.user.application.internal.storage.User;
import org.junit.Test;

import static org.junit.Assert.*;


public class SimpleUserDTOMapperTest {

    private SimpleUserDTOMapper simpleUserDTOMapper = new SimpleUserDTOMapper();

    @Test
    public void should_map_user_to_dto() {
        //given:
        User user = new User("123");
        user.assignEmailToBeConfirmed("john@doe.com", "token123");
        user.setResetPasswordToken("ttt555");
        user.setFullName("John Doe");
        //when:
        UserDTO dto = simpleUserDTOMapper.map(user);
        //then:
        assertEquals(dto.getResetPasswordToken(), user.getResetPasswordToken());
        assertEquals(dto.getId(), user.getId());
    }
}