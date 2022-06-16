package project.terminalv2.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdRequest {

    @NotBlank
    private String password;

    @NotBlank
    private String chkPwd;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

}
