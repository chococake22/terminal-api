package project.terminalv2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import project.terminalv2.dto.BoardUpdRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseTime {

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

//    @CreatedDate
//    @Column(updatable = false)
//    private LocalDateTime writeDate;
//
//    @Column
//    private LocalDateTime updateDate;

    @NotBlank
    @Column
    private String content;

    @ElementCollection
    @Column
    private List<Long> fileNos;

    @ElementCollection
    @Column
    private List<Long> commentNos;

    public void update(BoardUpdRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }


}
