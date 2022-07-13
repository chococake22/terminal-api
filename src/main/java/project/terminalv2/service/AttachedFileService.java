package project.terminalv2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import project.terminalv2.domain.AttachedFile;
import project.terminalv2.domain.Board;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.AttachedFileRepository;
import project.terminalv2.respository.BoardRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachedFileService {

    private final AttachedFileRepository attachedFileRepository;
    private final BoardRepository boardRepository;
    private final UserService userService;

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    @Transactional
    public ResponseEntity saveFiles(List<MultipartFile> files, Long boardNo, HttpServletRequest tokenInfo) throws IOException {

        if (files.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_FOUND_FILE);
        }

        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BOARD));


        if (userService.hasAccessAuth(board.getWriter(), tokenInfo)) {
            List<AttachedFile> attachedFiles = new ArrayList<>();

            for (MultipartFile file : files) {

                // 원래 파일명
                String orginalFilename = StringUtils.cleanPath(file.getOriginalFilename());

                // 저장 파일명
                String storeFileName = createStoreFileName(orginalFilename);

                // 경로로 파일 저장
                file.transferTo(new File(getFullPath(storeFileName)));

                // 파일 첨부 객체 생성
                AttachedFile attachedFile = AttachedFile.builder()
                        .filename(orginalFilename)
                        .saveName(storeFileName)
                        .board(board)
                        .build();

                // 파일 첨부 객체 저장
                attachedFileRepository.save(attachedFile);
            }
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.MULTIPART_FORM_DATA).body("성공");
            // 파일의 경우 contentType를 따로 지정해야 하기 때문에 ApiResponse로 리턴하는 것보다 기존의 ResponseEntity를 이용하는 것이 낫다고 판단.
        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }
    }

    // 저장명 만들기
    private String createStoreFileName(String orginalFilename) {

        String ext = extractExt(orginalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 확장자만 따로 추출하기
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    @Transactional
    public ResponseEntity<Resource> downloadFile(Long fileNo) throws MalformedURLException, UnsupportedEncodingException {

        AttachedFile file = attachedFileRepository.findById(fileNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_FILE));

        // 파일 이름
        String saveFileName = URLDecoder.decode(file.getFilename(), "UTF-8") ;
        log.info("saveFileName = {}", saveFileName);

        // 파일 uuid
        String uuidFileName = file.getSaveName();
        log.info("uuidFileName = {}", uuidFileName);

        UrlResource resource = new UrlResource("file:" + getFullPath(uuidFileName));

        String encodedUploadFileName = UriUtils.encode(saveFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.MULTIPART_FORM_DATA) // 타입을 Multipart로 설정
                .body(resource);
        // 파일의 경우 contentType를 따로 지정해야 하기 때문에 ApiResponse로 리턴하는 것보다 기존의 ResponseEntity를 이용하는 것이 낫다고 판단.
    }
}
