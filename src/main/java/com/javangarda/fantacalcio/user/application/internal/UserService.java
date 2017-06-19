package com.javangarda.fantacalcio.user.application.internal;


import com.javangarda.fantacalcio.user.application.data.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.data.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    UserDTO saveUser(RegisterUserCommand registerUserCommand);
    Optional<UserDTO> getByConfirmationToken(String confirmationToken);
    void confirmUserEmail(String email);
}
