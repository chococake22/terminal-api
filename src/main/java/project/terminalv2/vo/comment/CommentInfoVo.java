package project.terminalv2.vo.comment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentInfoVo {

    private Long commentNo;
    private String writer;
    private String content;
    private LocalDateTime writeDate;
}
