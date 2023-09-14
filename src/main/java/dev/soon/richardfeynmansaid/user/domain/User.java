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

    private String nickname;

    private String snsType;

    private String imageUrl;

    private String password;

    public User(SignupForm form) {
        this.email = form.email();
        this.nickname = form.email().split("@")[0];
        this.snsType = "email";
        this.password = form.password();
    }

    public User(String email, String nickname, String imageUrl, String oauth2Provider) {
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.snsType = oauth2Provider;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNickname() {
        return this.nickname;
    }

    public boolean isPasswordValid(LoginForm form, PasswordEncoder encoder) {
        return encoder.matches(form.password(), this.password);
    }
}
