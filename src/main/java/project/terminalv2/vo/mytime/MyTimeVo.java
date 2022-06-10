package project.terminalv2.vo.mytime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyTimeVo {

    private String startTarget;
    private String endTarget;
    private String startDate;
    private String busCorp;
    private String note;


}
