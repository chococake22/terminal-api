package project.terminalv2.vo.mytime;

import lombok.Builder;
import lombok.Data;
import project.terminalv2.domain.MyTime;

@Data
@Builder
public class MyTimeVo {

    private String startTarget;
    private String arrivedTarget;
    private String startDate;
    private String busCorp;
    private String layover;
    private String note;



}
