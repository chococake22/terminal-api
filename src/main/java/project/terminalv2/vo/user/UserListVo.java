package project.terminalv2.vo.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserListVo {

    private Long userNo;
    private String userId;
    private String username;
    private String email;
    private String phone;

}
