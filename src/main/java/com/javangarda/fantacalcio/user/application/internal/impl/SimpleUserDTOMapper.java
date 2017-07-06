package com.javangarda.fantacalcio.user.application.internal.impl;


import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.UserDTOMapper;
import com.javangarda.fantacalcio.user.application.internal.storage.User;

public class SimpleUserDTOMapper implements UserDTOMapper {
    @Override
    public UserDTO map(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setConfirmationToken(user.getConfirmEmailToken());
        dto.setConfirmedEmail(user.getEmail());
        dto.setUnConfirmedEmail(user.getTmpEmail());
        dto.setEmailLocale(user.getEmailLocale());
        dto.setResetPasswordToken(user.getResetPasswordToken());
        return dto;
    }
}
