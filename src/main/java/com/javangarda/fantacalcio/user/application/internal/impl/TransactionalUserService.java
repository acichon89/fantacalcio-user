package com.javangarda.fantacalcio.user.application.internal.impl;

import com.javangarda.fantacalcio.user.application.data.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.data.dto.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.UserFactory;
import com.javangarda.fantacalcio.user.application.internal.UserService;
import com.javangarda.fantacalcio.user.application.storage.User;
import com.javangarda.fantacalcio.user.application.storage.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Transactional
public class TransactionalUserService implements UserService {
    private final UserFactory userFactory;
    private final UserRepository userRepository;

    @Override
    public UserDTO saveUser(RegisterUserCommand registerUserCommand) {
        return map(userRepository.save(userFactory.create(registerUserCommand)));
    }

    @Override
    public Optional<UserDTO> getByConfirmationToken(String confirmationToken) {
        return userRepository.findByConfirmEmailToken(confirmationToken).map(this::map);
    }

    @Override
    public void confirmUserEmail(String email) {
        userRepository.findByTmpEmail(email).ifPresent(user -> {
            user.confirmEmail();
        });
    }

    private UserDTO map(User user){
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setConfirmationToken(user.getConfirmEmailToken());
        dto.setConfirmedEmail(user.getEmail());
        dto.setUnConfirmedEmail(user.getTmpEmail());
        dto.setEmailLocale(user.getEmailLocale());
        return dto;
    }
}
