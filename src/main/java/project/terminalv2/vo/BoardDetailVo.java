package project.terminalv2.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BoardDetailVo {

    private Long boardNo;
    private String title;
    private LocalDateTime writeDate;
    private LocalDateTime updateDate;
    private String writer;
    private String content;
    private List<Long> fileNos;
}
