package project.terminalv2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdRequest {

    private String password;

    private String chkPwd;

    @Email
    private String email;

    private String phone;

}
