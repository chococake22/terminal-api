package project.terminalv2.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.terminalv2.service.AttachedFileService;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AttachedFileController {

    private final AttachedFileService attachedFileService;

    @ApiOperation(value = "파일 저장", notes = "첨부 파일을 저장합니다.")
    @PostMapping(value = "/api/v1/board/{boardNo}", consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity saveFile(@RequestPart List<MultipartFile> files, @RequestParam Long boardNo) throws IOException {
        return attachedFileService.saveFiles(files, boardNo);
    }
}
