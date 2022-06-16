package project.terminalv2.vo.mytime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyTimeVo {

    private String startTarget;
    private String arrivedTarget;
    private String startTime;
    private String busCorp;
    private String layover;
    private String note;

}
