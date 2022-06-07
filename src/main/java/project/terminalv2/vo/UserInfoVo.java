package project.terminalv2.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserInfoVo {

    private Long userNo;
    private String userId;
    private String username;
    private String email;
    private String phone;

}
