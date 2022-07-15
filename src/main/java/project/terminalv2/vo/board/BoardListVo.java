package project.terminalv2.vo.board;

import lombok.Builder;
import lombok.Data;
import project.terminalv2.domain.BoardType;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardListVo {

    private Long boardNo;
    private String title;
    private BoardType boardType;
    private LocalDateTime writeDate;
    private String writer;
}
