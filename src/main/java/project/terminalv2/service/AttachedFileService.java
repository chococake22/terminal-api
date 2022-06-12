package project.terminalv2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import project.terminalv2.domain.AttachedFile;
import project.terminalv2.domain.Board;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.AttachedFileRepository;
import project.terminalv2.respository.BoardRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachedFileService {

    private final AttachedFileRepository attachedFileRepository;
    private final BoardRepository boardRepository;

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public ResponseEntity saveFiles(List<MultipartFile> files, Long boardNo) throws IOException {

        if (files.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_FOUND_FILE);
        }

        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BOARD));

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
}
