package project.terminalv2.domain;

import lombok.AllArgsConstructor;
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
public class AttachedFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_no")
    private Long fileNo;

    @NotBlank
    @Column
    private String filename;

    @NotBlank
    @Column
    private String fileSaveName;

    @CreatedDate
    @NotBlank
    @Column(updatable = false)
    private LocalDateTime savedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Board board;
}
