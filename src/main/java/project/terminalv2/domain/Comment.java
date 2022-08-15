package project.terminalv2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.terminalv2.dto.comment.CommentUpdRequest;
import project.terminalv2.vo.comment.CommentInfoVo;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_no")
    private Long commentNo;

    @NotBlank
    @Column(name = "writer")
    private String writer;

    @NotBlank
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no")
    private Board board;

    public void update(CommentUpdRequest request) {
        this.content = request.getContent();
    }

    public CommentInfoVo toCommentInfoVo(Comment comment) {
        return CommentInfoVo.builder()
                .commentNo(comment.getCommentNo())
                .writer(comment.getWriter())
                .content(comment.getContent())
                .writeDate(comment.getCreatedDate())
                .build();
    }
}
