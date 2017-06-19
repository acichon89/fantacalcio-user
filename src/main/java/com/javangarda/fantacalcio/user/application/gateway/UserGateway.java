package com.javangarda.fantacalcio.user.application.gateway;


import com.javangarda.fantacalcio.user.application.data.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.data.dto.UserDTO;

import java.util.Optional;

public interface UserGateway {
    //command:
    void registerUser(RegisterUserCommand registerUserCommand);
    void confirmUserEmail(String email);

    //query:
    Optional<UserDTO> getByConfirmationToken(String confirmationToken);
}
