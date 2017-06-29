package com.javangarda.fantacalcio.user.application.gateway;

import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;

import java.util.Optional;

public interface QueryFacade {
    Optional<UserDTO> getByConfirmationTokenAndEmail(String confirmationToken, String email);
}
