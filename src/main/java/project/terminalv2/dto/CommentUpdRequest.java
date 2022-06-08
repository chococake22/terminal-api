package project.terminalv2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class CommentUpdRequest {

    @NotEmpty
    private String content;

}
