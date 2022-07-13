package project.terminalv2.dto.user;


import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.terminalv2.domain.User;

import javax.validation.constraints.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveRequest {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]{6,14}",
            message = "아이디는 영문, 숫자만 가능하며 6 ~ 15자리까지 가능합니다.")
    private String userId;

    @NotBlank
    private String password;

    @NotBlank
    private String chkPwd;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]{4,9}",
            message = "이름은 영문, 숫자만 가능하며 4 ~ 10자리까지 가능합니다.")
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    public User createUser(UserSaveRequest request) {
        return User.builder()
                .userId(request.getUserId())
                .password(new BCryptPasswordEncoder().encode(request.getPassword()))
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
    }

}
