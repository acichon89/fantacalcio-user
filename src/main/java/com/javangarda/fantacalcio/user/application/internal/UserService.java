package com.javangarda.fantacalcio.user.application.internal;


import com.javangarda.fantacalcio.user.application.gateway.command.StartChangingEmailProcedureCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;

public interface UserService {
    UserDTO saveUser(RegisterUserCommand registerUserCommand);
    void confirmUserEmail(String token, String email);
    UserDTO storeTmpEmail(StartChangingEmailProcedureCommand startChangingEmailProcedureCommand);
    UserDTO assignResetPasswordToken(String email);

    void removeResetPasswordToken(String email);
}
