package project.terminalv2.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;

}
