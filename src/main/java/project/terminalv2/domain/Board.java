package project.terminalv2.domain;

import lombok.*;
import project.terminalv2.dto.BoardUpdRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_no")
    private Long boardNo;

    @NotBlank
    @Column
    private String title;

    @NotBlank
    @Column
    private String writer;

    @NotBlank
    @Column
    private String content;

    @OneToMany(mappedBy = "board")
    private List<AttachedFile> attachedFiles = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    public void update(BoardUpdRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }


}
