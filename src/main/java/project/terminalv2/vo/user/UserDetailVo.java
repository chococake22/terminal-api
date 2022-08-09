package project.terminalv2.vo.user;

import lombok.Builder;
import lombok.Data;
import project.terminalv2.domain.type.RoleType;

@Data
@Builder
public class UserDetailVo {

    private Long userNo;
    private String userId;
    private String username;
    private String email;
    private String phone;
    private RoleType role;
}
