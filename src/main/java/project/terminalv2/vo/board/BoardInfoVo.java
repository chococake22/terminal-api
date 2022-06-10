package project.terminalv2.vo.board;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardInfoVo {

    private Long boardNo;
    private String title;
    private LocalDateTime writeDate;
    private String writer;
}
