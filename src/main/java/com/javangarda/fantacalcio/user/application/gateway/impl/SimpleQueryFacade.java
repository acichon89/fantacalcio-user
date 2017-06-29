package com.javangarda.fantacalcio.user.application.gateway.impl;

import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.gateway.QueryFacade;
import com.javangarda.fantacalcio.user.application.internal.UserDTOMapper;
import com.javangarda.fantacalcio.user.application.internal.storage.UserRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class SimpleQueryFacade implements QueryFacade {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;


    @Override
    public Optional<UserDTO> getByConfirmationTokenAndEmail(String confirmationToken, String email) {
        return userRepository.findByConfirmEmailTokenAndEmail(confirmationToken,email).map(userDTOMapper::map);
    }
}
