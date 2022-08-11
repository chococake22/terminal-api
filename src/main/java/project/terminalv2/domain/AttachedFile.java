package project.terminalv2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "attached_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachedFile extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_no")
    private Long fileNo;

    // 파일명
    @NotBlank
    @Column(name = "filename")
    private String filename;

    // uuid
    @NotBlank
    @Column(name = "save_name")
    private String saveName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Board board;
}
