package project.terminalv2.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
    private String userId;

    @NotBlank
    @Column
    private String password;

    @NotBlank
    @Column
    private String username;

    @NotBlank
    @Column
    @Email
    private String email;

    @NotBlank
    @Column
    private String phone;

    @ElementCollection
    @Column
    private List<Long> myBusTimeNos;


}
