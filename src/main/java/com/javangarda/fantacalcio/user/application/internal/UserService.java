package com.javangarda.fantacalcio.user.application.internal;


import com.javangarda.fantacalcio.user.application.gateway.command.ChangeEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;

public interface UserService {
    UserDTO saveUser(RegisterUserCommand registerUserCommand);
    void confirmUserEmail(String email);
    UserDTO storeTmpEmail(ChangeEmailCommand changeEmailCommand);

}
