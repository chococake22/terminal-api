package project.terminalv2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Data
@Table(name = "bus_time")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class BusTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_time_no")
    private Long busTimeNo;

    @NotBlank
    @Column(name = "start_target")
    private String startTarget;

    @NotBlank
    @Column(name = "arrived_target")
    private String arrivedTarget;

    @NotBlank
    @Column(name = "start_date")
    private String startDate;

    @NotBlank
    @Column(name = "bus_corp")
    private String busCorp;

    @NotBlank
    @Column(name = "note")
    private String note;
}
