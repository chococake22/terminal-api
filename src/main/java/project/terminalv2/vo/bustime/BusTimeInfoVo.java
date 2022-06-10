package project.terminalv2.vo.bustime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusTimeInfoVo {

    private String startTarget;
    private String endTarget;
    private String startDate;
    private String busCorp;
    private String note;

}
