package project.terminalv2.dto.user;


import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Builder
public class UserSaveRequest {

    @NotEmpty
    @Size(min = 6, max = 15)
    private String userId;

    @NotEmpty
    private String password;

    @NotEmpty
    private String chkPwd;

    @NotEmpty
    private String username;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String phone;

}
