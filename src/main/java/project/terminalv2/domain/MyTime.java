package project.terminalv2.domain;

import lombok.*;
import project.terminalv2.vo.mytime.MyTimeVo;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_time_no")
    private Long myTimeNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_time_no")
    private BusTime busTime;


    public MyTimeVo toMyTimeVo(MyTime myTime) {
        return MyTimeVo.builder()
                .startTarget(myTime.getBusTime().getStartTarget())
                .arrivedTarget(myTime.getBusTime().getArrivedTarget())
                .startDate(myTime.getBusTime().getStartDate())
                .busCorp(myTime.getBusTime().getBusCorp())
                .layover(myTime.getBusTime().getLayover())
                .note(myTime.getBusTime().getNote())
                .build();
    }
}
