package project.terminalv2.dto.user;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
@Builder
public class UserUpdRequest {

    private String password;

    private String chkPwd;

    @Email
    private String email;

    private String phone;

}
