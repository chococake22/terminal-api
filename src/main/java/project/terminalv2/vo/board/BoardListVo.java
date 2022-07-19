package project.terminalv2.vo.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.terminalv2.domain.type.BoardType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListVo {

    private Long boardNo;
    private String title;
    private BoardType boardType;
    private LocalDateTime writeDate;
    private String writer;

    public BoardListVo(Long boardNo, String title, BoardType boardType, String writer) {
        this.boardNo = boardNo;
        this.title = title;
        this.boardType = boardType;
        this.writer = writer;
    }
}
