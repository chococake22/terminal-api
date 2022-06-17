package project.terminalv2.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.terminalv2.domain.AttachedFile;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.AttachedFileRepository;
import project.terminalv2.service.AttachedFileService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AttachedFileController {

    private final AttachedFileService attachedFileService;
    private final AttachedFileRepository attachedFileRepository;

    // 본인 확인 필요
    @ApiOperation(value = "파일 저장", notes = "첨부 파일을 저장합니다.")
    @PostMapping(value = "/api/v1/board/{boardNo}", consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity saveFile(@RequestPart List<MultipartFile> files, @RequestParam Long boardNo, HttpServletRequest tokenInfo) throws IOException {
        return attachedFileService.saveFiles(files, boardNo, tokenInfo);
    }

    // 파일 다운로드
    @ApiOperation(value = "파일 다운로드", notes = "첨부 파일을 다운로드합니다.")
    @GetMapping(value = "/api/v1/board/file/{fileNo}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileNo) throws MalformedURLException, UnsupportedEncodingException {
        return attachedFileService.downloadFile(fileNo);
    }
}
