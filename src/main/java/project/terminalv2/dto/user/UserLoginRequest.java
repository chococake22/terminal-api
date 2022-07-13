package project.terminalv2.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]{6,14}",
            message = "아이디는 영문, 숫자만 가능하며 6 ~ 15자리까지 가능합니다.")
    private String userId;

    @NotBlank
    private String password;

}
