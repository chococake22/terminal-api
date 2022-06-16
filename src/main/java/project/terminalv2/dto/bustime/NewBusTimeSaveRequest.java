package project.terminalv2.dto.bustime;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class NewBusTimeSaveRequest {

    @NotBlank
    private String startTarget;

    @NotBlank
    private String endTarget;

    @NotBlank
    private String startTime;

    @NotBlank
    private String busCorp;

    private String layover;

    private String note;
}
