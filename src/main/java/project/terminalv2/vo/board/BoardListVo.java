package project.terminalv2.vo.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import project.terminalv2.domain.Board;
import project.terminalv2.domain.type.BoardType;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListVo {

    private Long boardNo;
    private String title;
    private BoardType boardType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
    private String writer;

    public BoardListVo(Long boardNo, String title, BoardType boardType, String writer) {
        this.boardNo = boardNo;
        this.title = title;
        this.boardType = boardType;
        this.writer = writer;
    }

    public BoardListVo(Board board) {
        boardNo = board.getBoardNo();
        title = board.getTitle();
        boardType = board.getBoardType();
        createdDate = board.getCreatedDate();
        writer = board.getWriter();
    }
}
