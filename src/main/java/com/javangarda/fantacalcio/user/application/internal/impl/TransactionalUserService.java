package com.javangarda.fantacalcio.user.application.internal.impl;

import com.javangarda.fantacalcio.user.application.gateway.command.StartChangingEmailProcedureCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.AccessTokenGenerator;
import com.javangarda.fantacalcio.user.application.internal.UserDTOMapper;
import com.javangarda.fantacalcio.user.application.internal.UserFactory;
import com.javangarda.fantacalcio.user.application.internal.UserService;
import com.javangarda.fantacalcio.user.application.internal.storage.model.User;
import com.javangarda.fantacalcio.user.application.internal.storage.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Transactional
public class TransactionalUserService implements UserService {
    private final UserFactory userFactory;
    private final UserRepository userRepository;
    private final AccessTokenGenerator accessTokenGenerator;
    private final UserDTOMapper userDTOMapper;

    @Override
    public UserDTO saveUser(RegisterUserCommand registerUserCommand) {
        return userDTOMapper.map(userRepository.save(userFactory.create(registerUserCommand)));
    }

    @Override
    public void confirmUserEmail(String token, String email) {
        userRepository.findByTmpEmailAndToken(token, email).ifPresent(user -> user.confirmEmail());
    }

    @Override
    public UserDTO storeTmpEmail(StartChangingEmailProcedureCommand startChangingEmailProcedureCommand) {
        User u = userRepository.findByEmail(startChangingEmailProcedureCommand.getOldEmail()).get();
        u.assignEmailToBeConfirmed(startChangingEmailProcedureCommand.getNewEmail(), accessTokenGenerator.createConfirmEmailToken());
        return userDTOMapper.map(u);
    }

    @Override
    public UserDTO assignResetPasswordToken(String email) {
        User user = userRepository.findByEmail(email).get();
        user.setResetPasswordToken(accessTokenGenerator.createResetPasswordToken());
        return userDTOMapper.map(user);
    }

    @Override
    public void removeResetPasswordToken(String email) {
        userRepository.findByEmail(email).ifPresent(user -> user.clearResetPasswordToken());
    }


}
