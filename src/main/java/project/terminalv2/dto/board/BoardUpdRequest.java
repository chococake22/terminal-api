package project.terminalv2.dto.board;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
public class BoardUpdRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

}
