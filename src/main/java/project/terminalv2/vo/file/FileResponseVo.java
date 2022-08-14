package project.terminalv2.vo.file;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class FileResponseVo {

    private String filename;
    private String fileUri;
}
