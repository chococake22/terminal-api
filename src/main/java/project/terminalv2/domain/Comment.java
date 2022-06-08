package project.terminalv2.domain;

import lombok.*;
import project.terminalv2.dto.CommentUpdRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

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
    @Column
    private String writer;

    @NotBlank
    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Board board;

    public void update(CommentUpdRequest request) {
        this.content = request.getContent();
    }
}
