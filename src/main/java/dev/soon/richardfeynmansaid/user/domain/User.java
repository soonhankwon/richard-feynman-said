package dev.soon.richardfeynmansaid.user.domain;

import dev.soon.richardfeynmansaid.auth.controller.dto.LoginForm;
import dev.soon.richardfeynmansaid.user.controller.dto.SignupForm;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@NoArgsConstructor
@ToString
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    public User(SignupForm form) {
        this.email = form.email();
        this.password = form.password();
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isPasswordValid(LoginForm form, PasswordEncoder encoder) {
        return encoder.matches(form.password(), this.password);
    }
}
