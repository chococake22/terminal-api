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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import project.terminalv2.domain.AttachedFile;
import project.terminalv2.domain.Board;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ApiResponse;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.AttachedFileRepository;
import project.terminalv2.respository.BoardRepository;
import project.terminalv2.vo.file.FileResponseVo;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachedFileService {

    private final AttachedFileRepository attachedFileRepository;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final ApiResponse apiResponse;

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    @Transactional
    public ApiResponse saveFiles(List<MultipartFile> files, Long boardNo, HttpServletRequest tokenInfo) throws IOException {

        if (files.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_FOUND_FILE);
        }

        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BOARD));

        if (userService.hasAccessAuth(board.getWriter(), tokenInfo)) {

            List<FileResponseVo> fileResponseVos = new ArrayList<>();

            String downloadURI = "";

            for (MultipartFile file : files) {

                // 원래 파일명
                String orginalFilename = StringUtils.cleanPath(file.getOriginalFilename());

                // 저장 파일명
                String storeFileName = createStoreFileName(orginalFilename);

                // 경로로 파일 저장
                file.transferTo(new File(getFullPath(orginalFilename)));

                // 파일 첨부 객체 생성
                AttachedFile attachedFile = AttachedFile.builder()
                        .filename(orginalFilename)
                        .saveName(storeFileName)
                        .board(board)
                        .build();

                // URI 생성
                downloadURI = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/board/download/")
                        .path(attachedFile.getFilename())
                        .toUriString();

                FileResponseVo fileResponseVo = FileResponseVo.builder()
                        .filename(orginalFilename.toString())
                        .fileUri(downloadURI.toString())
                        .build();

                fileResponseVos.add(fileResponseVo);

                // 파일 첨부 객체 저장
                attachedFileRepository.save(attachedFile);
            }

            return apiResponse.makeResponse(HttpStatus.OK, "6000", "파일 업로드 성공", fileResponseVos);

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
        int pos = originalFilename.lastIndexOf(".");    // .의 인덱스 찾기
        return originalFilename.substring(pos + 1); // . 이후 문자열이 확장자가 된다
    }

    @Transactional
    public ResponseEntity<Resource> downloadFile(String fileName) throws MalformedURLException, UnsupportedEncodingException {

        UrlResource resource = new UrlResource("file:" + getFullPath(fileName));

        log.info("URL: {}", resource.toString());

        String encodedUploadFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);

        log.info("encodedUploadFileName: {}", encodedUploadFileName);

        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.MULTIPART_FORM_DATA) // 타입을 Multipart로 설정
                .body(resource);
        // 파일의 경우 contentType를 따로 지정해야 하기 때문에 ApiResponse로 리턴하는 것보다 기존의 ResponseEntity를 이용하는 것이 낫다고 판단.
    }

    @Transactional
    public ApiResponse deleteFile(Long boardNo, String fileName, HttpServletRequest tokenInfo) {

        String userId = userService.getUserIdFromToken(tokenInfo);

        // 해당 파일이 속한 게시글 조회
        Board board = boardService.getBoard(boardNo);

        // 작성자의 userNo와 로그인한 사용자의 userNo가 같으면 삭제함.
        if (userId.equals(board.getWriter())) {

            // 경로 생성
            String path = getFullPath(fileName);

            // 해당 경로에 있는 파일 가져오기
            File file = new File(path);

            // 파일 삭제
            file.delete();

            // 파일 정보 객체 존재 여부 파악
            AttachedFile attachedFile = attachedFileRepository.findByBoard_BoardNoAndFilename(boardNo, fileName)
                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_FILE_INFO));

            // 파일 정보 객체 삭제
            attachedFileRepository.delete(attachedFile);

            return apiResponse.makeResponse(HttpStatus.OK, "6000", "파일 삭제 성공", null);

        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
