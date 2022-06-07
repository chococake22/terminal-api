package project.terminalv2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_no")
    private Long commentNo;

    @NotBlank
    @Column
    private String writer;

    @NotBlank
    @Column
    private LocalDateTime writeDate;

    @NotBlank
    @Column
    private String content;

    @ElementCollection
    @Column
    private List<Long> reCommentNo;
}
