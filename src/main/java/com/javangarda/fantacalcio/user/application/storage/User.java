package com.javangarda.fantacalcio.user.application.storage;

import com.javangarda.fantacalcio.commons.entities.VersionedDefaultEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.ws.rs.GET;
import java.util.Locale;

@Entity
@Table(name = "users")
@ToString
public class User extends VersionedDefaultEntity <String> {

    @Getter
    private String email;
    @Getter
    private String tmpEmail;
    @Getter @Setter
    private String fullName;
    @Getter
    @Column(name="confirm_email_token")
    private String confirmEmailToken;
    @Setter
    private String resetPasswordToken;
    @Getter
    private Locale emailLocale;

    public User() {super();};

    public User(String id){
        super(id);
    }

    public void register(String fullName, String email, String confirmationToken, Locale emailLocale){
        this.fullName=fullName;
        this.tmpEmail=email;
        this.confirmEmailToken=confirmationToken;
        this.emailLocale=emailLocale;
    }

    public void confirmEmail() {
        this.email=this.tmpEmail;
        this.tmpEmail=null;
        this.confirmEmailToken=null;
    }

    public boolean hasConfirmationEmailToken(String token){
        return this.confirmEmailToken == null ? token == null : this.confirmEmailToken.equals(token);
    }
}
