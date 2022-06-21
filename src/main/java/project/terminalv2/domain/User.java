package project.terminalv2.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import project.terminalv2.dto.user.UserUpdRequest;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long userNo;

    @NotBlank
    @Column(name = "user_id")
    private String userId;

    @NotBlank
    @Column(name = "password")
    private String password;

    @NotBlank
    @Column(name = "username")
    private String username;

    @NotBlank
    @Column(name = "email")
    @Email
    private String email;

    @NotBlank
    @Column(name = "phone")
    private String phone;

    // 시간표를 삭제할 경우 내 시간표에 있는 해당 시간표도 삭제되어야 한다.
    @OneToMany(mappedBy = "myTimeNo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyTime> myBusTime = new ArrayList<>();

    @Transactional
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

}
