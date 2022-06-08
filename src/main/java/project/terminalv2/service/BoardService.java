package project.terminalv2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.terminalv2.domain.Board;
import project.terminalv2.dto.BoardSaveRequest;
import project.terminalv2.dto.BoardUpdRequest;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.BoardRepository;
import project.terminalv2.vo.BoardDetailVo;
import project.terminalv2.vo.BoardInfoVo;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public ResponseEntity getBoardInfoOne(Long no) {

        // 게시글 검색
        // 해당 게시글이 없으면 예외처리
        Board board = boardRepository.findById(no)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BOARD));

        log.info("Board = {}", board);

        BoardDetailVo boardDetailVo = BoardDetailVo.builder()
                .boardNo(board.getBoardNo())
                .title(board.getTitle())
                .writeDate(board.getCreatedDate())
                .updateDate(board.getModifiedDate())
                .writer(board.getWriter())
                .content(board.getContent())
                .fileNos(board.getFileNos())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(boardDetailVo);
    }

    @Transactional
    public ResponseEntity getBoardList(Integer page, Integer size) {

        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardNo"));
        Page<Board> boards = boardRepository.findAll(pageable);
        Page<BoardInfoVo> boardInfoVos = boards.map(board -> BoardInfoVo.builder()
                .boardNo(board.getBoardNo())
                .title(board.getTitle())
                .writer(board.getWriter())
                .build());

        return ResponseEntity.status(HttpStatus.OK).body(boardInfoVos);
    }

    @Transactional
    public ResponseEntity saveBoard(BoardSaveRequest request) {

        Board board = Board.builder()
                .title(request.getTitle())
                .writer("apple")
                .content(request.getContent())
                .build();

        boardRepository.save(board);

        return ResponseEntity.status(HttpStatus.OK).body("글 작성 성공");
    }


    @Transactional
    public ResponseEntity updateBoard(Long boardNo, BoardUpdRequest request) {

        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BOARD));

        board.update(request);

        return ResponseEntity.status(HttpStatus.OK).body("글 수정 성공");
    }


    public ResponseEntity deleteBoard(Long boardNo) {

        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BOARD));

        boardRepository.deleteById(boardNo);

        return ResponseEntity.status(HttpStatus.OK).body("글 삭제 성공");

    }
}
