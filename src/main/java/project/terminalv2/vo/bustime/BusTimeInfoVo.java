package project.terminalv2.vo.bustime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusTimeInfoVo {

    private String startTarget;
    private String arrivedTarget;
    private String startTime;
    private String busCorp;
    private String layover;
    private String note;

}
