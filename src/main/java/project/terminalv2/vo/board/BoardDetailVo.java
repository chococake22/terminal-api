package project.terminalv2.vo.board;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime writeDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateDate;
    private String writer;
    private String content;
}
