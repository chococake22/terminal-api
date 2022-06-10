package project.terminalv2.dto.user;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Builder
public class UserLoginRequest {

    @NotEmpty
    @Size(min = 6, max = 15)
    private String userId;

    @NotEmpty
    private String password;

}
