package project.terminalv2.dto.board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.terminalv2.domain.type.BoardType;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardSaveRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private BoardType boardType;

    @NotEmpty
    private String content;

}
