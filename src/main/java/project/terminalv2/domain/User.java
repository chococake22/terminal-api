package project.terminalv2.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.terminalv2.domain.type.RoleType;
import project.terminalv2.dto.user.UserUpdRequest;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.vo.user.UserDetailVo;
import project.terminalv2.vo.user.UserListVo;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long userNo;

    @NotBlank
    @Column(name = "user_id")
    @Pattern(regexp = "[a-zA-Z0-9]{6,14}",
            message = "아이디는 영문, 숫자만 가능하며 6 ~ 15자리까지 가능합니다.")
    private String userId;

    @NotBlank
    @Column(name = "password")
    private String password;

    @NotBlank
    @Column(name = "username")
    @Pattern(regexp = "[a-zA-Z0-9]{4,9}",
            message = "이름은 영문, 숫자만 가능하며 4 ~ 10자리까지 가능합니다.")
    private String username;

    @NotBlank
    @Column(name = "email")
    @Email
    private String email;

    @NotBlank
    @Column(name = "phone")
    private String phone;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    // User 삭제시 관련 MyTime도 같이 삭제
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyTime> myBusTime = new ArrayList<>();


    public void updateInfo(UserUpdRequest request) {

        // 두 개의 확인 비밀번호가 같아야 한다.
        if (request.getPassword().equals(request.getChkPwd())) {
            this.password = new BCryptPasswordEncoder().encode(request.getPassword());
            this.email = request.getEmail();
            this.phone = request.getPhone();
        } else {
            throw new ApiException(ErrorCode.NOT_EQUAL_PWD);
        }
    }

    // UserListVo로 변환하는 메서드
    public UserListVo toUserListVo(User user) {

        return UserListVo.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    // UserDetailVo로 변환하는 메서드
    // 현재는 사용자 리스트 정보와 차이가 없으나
    // 추후에 사용자 상세정보는 더 많은 정보를 가져올 수 있으므로 메서드를 별도로 구현
    public UserDetailVo toUserDetailVo(User user) {

        return UserDetailVo.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}
