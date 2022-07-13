package project.terminalv2.vo.user;

import lombok.Builder;
import lombok.Data;
import project.terminalv2.domain.User;

import java.util.List;

@Data
@Builder
public class UserInfoVo {

    private Long userNo;
    private String userId;
    private String username;
    private String email;
    private String phone;

}
