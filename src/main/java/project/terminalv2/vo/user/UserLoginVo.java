package project.terminalv2.vo.user;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import project.terminalv2.domain.type.RoleType;

@Getter
@Builder
public class UserLoginVo {

    private Long userNo;
    private String userId;
    private RoleType role;
    private String accessToken;
    private String refreshToken;



}
