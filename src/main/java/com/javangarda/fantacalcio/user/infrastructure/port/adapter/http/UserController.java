package com.javangarda.fantacalcio.user.infrastructure.port.adapter.http;

import com.javangarda.fantacalcio.user.application.data.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.data.dto.UserDTO;
import com.javangarda.fantacalcio.user.application.gateway.UserGateway;
import com.javangarda.fantacalcio.user.infrastructure.port.adapter.http.model.RegistrationUserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.Locale;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserGateway userGateway;

    @PostMapping(value="/register")
    public ResponseEntity<RegistrationUserViewModel> registerUser(@RequestBody @Validated RegisterUserCommand registerUserCommand)  {
        userGateway.registerUser(registerUserCommand);
        return ResponseEntity.ok().body(RegistrationUserViewModel.of(true, registerUserCommand.getEmail()));
    }

    @GetMapping(value = "/unconfirmedUser")
    public ResponseEntity<UserDTO> getByConfirmationToken(@RequestParam("token") String confirmationToken, @RequestParam("email") String email) {
        return ResponseEntity.ok(userGateway.getByConfirmationTokenAndEmail(confirmationToken, email)
                .orElseThrow(() -> new ResourceNotFoundException("user", null)));
    }
}
