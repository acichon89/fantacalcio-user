package com.javangarda.fantacalcio.user.application.internal.impl;

import com.javangarda.fantacalcio.user.application.gateway.command.ChangeEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.AccessTokenGenerator;
import com.javangarda.fantacalcio.user.application.internal.UserDTOMapper;
import com.javangarda.fantacalcio.user.application.internal.UserFactory;
import com.javangarda.fantacalcio.user.application.internal.UserService;
import com.javangarda.fantacalcio.user.application.internal.storage.User;
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
    public void confirmUserEmail(String email) {
        userRepository.findByTmpEmail(email).ifPresent(user -> {
            user.confirmEmail();
        });
    }

    @Override
    public UserDTO storeTmpEmail(ChangeEmailCommand changeEmailCommand) {
        User u = userRepository.findByEmail(changeEmailCommand.getOldEmail()).get();
        u.assignEmailToBeConfirmed(changeEmailCommand.getNewEmail(), accessTokenGenerator.createConfirmEmailToken());
        return userDTOMapper.map(u);
    }


}
