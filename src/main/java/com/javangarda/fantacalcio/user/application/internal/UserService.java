package com.javangarda.fantacalcio.user.application.internal;


import com.javangarda.fantacalcio.user.application.data.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.data.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    UserDTO saveUser(RegisterUserCommand registerUserCommand);
    void confirmUserEmail(String email);

    Optional<UserDTO> getByConfirmationTokenAndEmail(String confirmationToken, String email);
}
