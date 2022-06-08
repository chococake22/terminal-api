package project.terminalv2.vo;

import lombok.Builder;
import lombok.Data;
import project.terminalv2.domain.Board;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentInfoVo {

    private Long commentNo;
    private String writer;
    private String content;
    private LocalDateTime writeDate;
}
