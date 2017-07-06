package com.javangarda.fantacalcio.user.infrastructure.port.adapter.http.api;

import com.javangarda.fantacalcio.user.application.gateway.CommandBus;
import com.javangarda.fantacalcio.user.application.gateway.QueryFacade;
import com.javangarda.fantacalcio.user.application.gateway.command.ChangeEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.infrastructure.port.adapter.http.model.ChangeEmailViewModel;
import com.javangarda.fantacalcio.user.infrastructure.port.adapter.http.model.RegistrationUserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserEndpoint {

    @Autowired
    private QueryFacade queryFacade;
    @Autowired
    private CommandBus commandBus;

    @PostMapping(value="/register")
    public ResponseEntity<RegistrationUserViewModel> registerUser(@RequestBody @Validated RegisterUserCommand registerUserCommand)  {
        commandBus.registerUser(registerUserCommand);
        return ResponseEntity.ok().body(RegistrationUserViewModel.of(true, registerUserCommand.getEmail()));
    }

    @PostMapping(value = "/changeEmail")
    public ResponseEntity<ChangeEmailViewModel> startChangeEmailProcedure(@RequestBody @Valid ChangeEmailCommand changeEmailCommand) {
        commandBus.startChangingEmailProcedure(changeEmailCommand);
        return ResponseEntity.ok(ChangeEmailViewModel.of(true, changeEmailCommand.getNewEmail()));
    }

    @GetMapping(value = "/unconfirmedUser")
    public ResponseEntity<UserDTO> getByConfirmationToken(@RequestParam("token") String confirmationToken, @RequestParam("email") String email) {
        return ResponseEntity.ok(queryFacade.getByConfirmationTokenAndEmail(confirmationToken, email)
                .orElseThrow(() -> new ResourceNotFoundException("user", null)));
    }
}
