package com.javangarda.fantacalcio.user.infrastructure.adapter.income.http;


import com.javangarda.fantacalcio.commons.authentication.CurrentUserResolver;
import com.javangarda.fantacalcio.commons.authentication.impl.SecurityContextCurrentUserResolver;
import com.javangarda.fantacalcio.commons.http.SuccessResponseDTO;
import com.javangarda.fantacalcio.user.application.gateway.CommandBus;
import com.javangarda.fantacalcio.user.application.gateway.QueryFacade;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.StartChangingEmailProcedureCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.StartResettingPasswordProcedureCommand;
import com.javangarda.fantacalcio.user.application.internal.impl.AuthClient;
import com.javangarda.fantacalcio.user.application.internal.storage.dataprojection.UserVerificationDataProjection;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
public class UserEndpoint {

    @Autowired
    private CommandBus commandBus;
    @Autowired
    private QueryFacade queryFacade;


    //@PreAuthorize("#oauth2.hasScope('gui')")
    @PostMapping(value="/register")
    public ResponseEntity<SuccessResponseDTO> registerUser(@RequestBody @Validated RegisterRequestModel requestModel)  {
        RegisterUserCommand command = RegisterUserCommand.of(requestModel.email, requestModel.fullName);
        commandBus.registerUser(command);
        return ResponseEntity.ok().body(new SuccessResponseDTO());
    }

    @PreAuthorize("#oauth2.hasScope('gui')")
    @PostMapping(value = "/startChangeEmailProcedure")
    public ResponseEntity<SuccessResponseDTO> startChangeEmailProcedute(@RequestBody @Validated StartChangingEmailProcedureRequestModel requestModel) {
        StartChangingEmailProcedureCommand command = StartChangingEmailProcedureCommand.of(requestModel.oldEmail, requestModel.newEmail);
        commandBus.startChangingEmailProcedure(command);
        return ResponseEntity.ok().body(new SuccessResponseDTO());
    }

    @PreAuthorize("#oauth2.hasScope('gui') and hasRole('ROLE_BASIC')")
    @GetMapping("/fancy-x-proto")
    public String currentUser(Principal principal){
        return new SecurityContextCurrentUserResolver().resolveCurrentUserIdentity().get();
    }

    @PreAuthorize("#oauth2.hasScope('gui')")
    @PostMapping(value = "/startResetPasswordProcedure")
    public ResponseEntity<SuccessResponseDTO> startResetPasswordProcedure(@RequestBody @Validated ResetPasswordRequestModel requestModel) {
        StartResettingPasswordProcedureCommand command = StartResettingPasswordProcedureCommand.of(requestModel.email);
        commandBus.startResetPasswordProcedure(command);
        return ResponseEntity.ok().body(new SuccessResponseDTO());
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @GetMapping(value = "/usersWithEmailToConfirm")
    public ResponseEntity<UserVerificationDataProjection> getByTmpEmailAndVerificationEmailToken(
            @RequestParam("email") String tmpEmail,
            @RequestParam("verificationToken") String verificationEmailToken) {
        Optional<UserVerificationDataProjection> potentialDataProjection = queryFacade.findByTmpEmailAndVerificationEmailToken(tmpEmail, verificationEmailToken);
        return potentialDataProjection.isPresent() ? ResponseEntity.ok(potentialDataProjection.get()) : (ResponseEntity<UserVerificationDataProjection>) ResponseEntity.notFound();
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @GetMapping(value = "/usersWithForgottenPassword")
    public ResponseEntity<UserVerificationDataProjection> getByEmailAndResetPasswordToken(
            @RequestParam("email") String email,
            @RequestParam("resetPasswordToken") String token) {
        Optional<UserVerificationDataProjection> potentialDataProjection = queryFacade.findByEmailAndResetPasswordToken(email, token);
        return potentialDataProjection.isPresent() ? ResponseEntity.ok(potentialDataProjection.get()) : (ResponseEntity<UserVerificationDataProjection>) ResponseEntity.notFound();
    }
}
