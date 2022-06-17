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
import project.terminalv2.domain.SearchType;
import project.terminalv2.dto.board.BoardSaveRequest;
import project.terminalv2.dto.board.BoardUpdRequest;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.BoardRepository;
import project.terminalv2.vo.board.BoardDetailVo;
import project.terminalv2.vo.board.BoardInfoVo;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final JwtService jwtService;
    private final UserService userService;

    @Transactional
    public ResponseEntity getBoardInfoOne(Long boardNo) {

        // 게시글 검색
        // 해당 게시글이 없으면 예외처리
        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BOARD));

        log.info("Board = {}", board);

        BoardDetailVo boardDetailVo = BoardDetailVo.builder()
                .boardNo(board.getBoardNo())
                .title(board.getTitle())
                .writeDate(board.getCreatedDate())
                .updateDate(board.getModifiedDate())
                .writer(board.getWriter())
                .content(board.getContent())
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
    public ResponseEntity saveBoard(BoardSaveRequest request, HttpServletRequest tokenInfo) {

        String token = tokenInfo.getHeader("jwt");
        String userId = jwtService.getSubject(token);

        Board board = Board.builder()
                .title(request.getTitle())
                .writer(userId)
                .content(request.getContent())
                .build();

        boardRepository.save(board);

        return ResponseEntity.status(HttpStatus.OK).body("save board success");
    }


    @Transactional
    public ResponseEntity updateBoard(Long boardNo, BoardUpdRequest request, HttpServletRequest tokenInfo) {

        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BOARD));

        if (userService.hasAccessAuth(board.getWriter(), tokenInfo)) {
            board.update(request);
            return ResponseEntity.status(HttpStatus.OK).body("update board success");
        } else {
            throw new ApiException(ErrorCode.BOARD_UNAUTHORIZED);
        }
    }


    public ResponseEntity deleteBoard(Long boardNo, HttpServletRequest tokenInfo) {

        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BOARD));

        if (userService.hasAccessAuth(board.getWriter(), tokenInfo)) {
            boardRepository.deleteById(boardNo);
            return ResponseEntity.status(HttpStatus.OK).body("delete board success");
        } else {
            throw new ApiException(ErrorCode.BOARD_UNAUTHORIZED);
        }
    }


    @Transactional
    public ResponseEntity searchBoard(Integer page, Integer size, Integer type, String search) {

        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardNo"));

        Page<Board> boards;

        // 카테고리별 검색
        if (SearchType.ofCode(type).equals(SearchType.TITLE)) {
            boards = boardRepository.findAllByTitleContaining(search, pageable);
        } else if (SearchType.ofCode(type).equals(SearchType.WRITER)) {
            boards = boardRepository.findAllByWriterContaining(search, pageable);
        } else if (SearchType.ofCode(type).equals(SearchType.CONTENT)) {
            boards = boardRepository.findAllByContentContaining(search, pageable);
        } else if(SearchType.ofCode(type).equals(SearchType.All) && search == null) {
            boards = boardRepository.findAll(pageable);
        } else {
            boards = boardRepository.findAllBySearch(search, pageable);
        }

        Page<BoardInfoVo> boardInfoVos = boards.map(board -> BoardInfoVo.builder()
                .boardNo(board.getBoardNo())
                .title(board.getTitle())
                .writer(board.getWriter())
                .build());

        return ResponseEntity.status(HttpStatus.OK).body(boardInfoVos);

    }
}
