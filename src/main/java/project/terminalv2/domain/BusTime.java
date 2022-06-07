package project.terminalv2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Data
@Table(name = "bus_time")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BusTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_time_no")
    private Long busTimeNo;

    @NotBlank
    @Column
    private String startTarget;

    @NotBlank
    @Column
    private String arrivedTarget;

    @NotBlank
    @Column
    private LocalDateTime startDate;

    @NotBlank
    @Column
    private String busCorp;

    @NotBlank
    @Column
    private String note;
}
