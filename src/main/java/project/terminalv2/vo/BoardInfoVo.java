package project.terminalv2.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import project.terminalv2.domain.Board;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class BoardInfoVo {

    private Long boardNo;
    private String title;
    private LocalDateTime writeDate;
    private String writer;

}
