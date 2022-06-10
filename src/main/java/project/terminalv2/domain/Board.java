package project.terminalv2.domain;

import lombok.*;
import project.terminalv2.dto.board.BoardUpdRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_no")
    private Long boardNo;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name = "writer")
    private String writer;

    @NotBlank
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    public void update(BoardUpdRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }
}
