package project.terminalv2.dto.user;


import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Builder
public class UserSaveRequest {

    @NotBlank
    @Size(min = 6, max = 15)
    private String userId;

    @NotBlank
    private String password;

    @NotBlank
    private String chkPwd;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

}
