package project.terminalv2.domain;

import lombok.*;
import project.terminalv2.domain.type.BoardType;
import project.terminalv2.dto.board.BoardUpdRequest;
import project.terminalv2.vo.board.BoardDetailVo;

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

    @Column(name = "board_type")
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name = "writer")
    private String writer;

    @NotBlank
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttachedFile> attachedFiles = new ArrayList<>();

    public void update(BoardUpdRequest request) {
        this.title = request.getTitle();
        this.boardType = BoardType.ofCode(request.getBoardTypeCode());
        this.content = request.getContent();
    }

    public BoardDetailVo toBoardDetailVo(Board board) {
        return BoardDetailVo.builder()
                .boardNo(board.getBoardNo())
                .title(board.getTitle())
                .boardType(board.getBoardType())
                .writer(board.getWriter())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .build();
    }
}
