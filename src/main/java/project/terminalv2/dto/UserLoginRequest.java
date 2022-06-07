package project.terminalv2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequest {

    @NotEmpty
    @Size(min = 6, max = 15)
    private String userId;

    @NotEmpty
    private String password;

}
