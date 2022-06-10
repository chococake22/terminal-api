package project.terminalv2.dto.bustime;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
public class NewBusTimeSaveRequest {

    @NotEmpty
    private String startTarget;

    @NotEmpty
    private String endTarget;

    @NotEmpty
    private String startDate;

    @NotEmpty
    private String busCorp;

    @NotEmpty
    private String note;
}
