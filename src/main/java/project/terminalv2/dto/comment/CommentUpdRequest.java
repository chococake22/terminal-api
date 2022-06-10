package project.terminalv2.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
public class CommentUpdRequest {

    @NotEmpty
    private String content;

}
