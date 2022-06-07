package project.terminalv2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_no")
    private Long boardNo;

    @NotBlank
    @Column
    private String title;

    @NotBlank
    @Column
    private String writer;

    @NotBlank
    @Column
    private LocalDateTime writeDate;

    @NotBlank
    @Column
    private LocalDateTime updateDate;

    @NotBlank
    @Column
    private String content;

    @ElementCollection
    @Column
    private List<Long> fileNos;

    @ElementCollection
    @Column
    private List<Long> commentNos;


}
