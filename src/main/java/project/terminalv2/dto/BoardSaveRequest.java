package project.terminalv2.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardSaveRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

}
