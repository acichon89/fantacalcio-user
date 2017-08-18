package com.javangarda.fantacalcio.user.application.internal;


import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.storage.model.User;

public interface UserDTOMapper {
    UserDTO map(User user);
}
