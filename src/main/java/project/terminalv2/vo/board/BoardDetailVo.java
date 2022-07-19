package project.terminalv2.vo.board;

import lombok.Builder;
import lombok.Data;
import project.terminalv2.domain.type.BoardType;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardDetailVo {

    private Long boardNo;
    private BoardType boardType;
    private String title;
    private LocalDateTime writeDate;
    private LocalDateTime updateDate;
    private String writer;
    private String content;
}
