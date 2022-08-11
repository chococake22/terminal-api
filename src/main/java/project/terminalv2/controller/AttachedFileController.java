package project.terminalv2.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.terminalv2.exception.ApiResponse;
import project.terminalv2.service.AttachedFileService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
public class AttachedFileController {

    private final AttachedFileService attachedFileService;

    // 본인 확인 필요
    @ApiOperation(value = "파일 저장", notes = "첨부 파일을 저장합니다.")
    @PostMapping(value = "/api/v1/board/file/{boardNo}", consumes = {MULTIPART_FORM_DATA_VALUE})
    public ApiResponse saveFile(@RequestPart List<MultipartFile> files, @RequestParam Long boardNo, HttpServletRequest tokenInfo) throws IOException {
        return attachedFileService.saveFiles(files, boardNo, tokenInfo);
    }

    // 파일 다운로드
    @ApiOperation(value = "파일 다운로드", notes = "첨부 파일을 다운로드합니다.")
    @GetMapping(value = "/api/v1/board/file/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws MalformedURLException, UnsupportedEncodingException {
        return attachedFileService.downloadFile(fileName);
    }

    // 파일 삭제
    @ApiOperation(value = "파일 삭제", notes = "첨부 파일을 삭제합니다.")
    @DeleteMapping(value = "/api/v1/board/file/{boardNo}/{fileName}")
    public ApiResponse deleteFile(@PathVariable Long boardNo, @PathVariable String fileName, HttpServletRequest tokenInfo) {
        return attachedFileService.deleteFile(boardNo, fileName, tokenInfo);
    }
}
