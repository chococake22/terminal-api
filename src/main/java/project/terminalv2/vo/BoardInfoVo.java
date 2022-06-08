package project.terminalv2.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
