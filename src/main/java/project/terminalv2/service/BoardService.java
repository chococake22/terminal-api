package project.terminalv2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.terminalv2.domain.Board;
import project.terminalv2.domain.type.BoardType;
import project.terminalv2.domain.type.SearchType;
import project.terminalv2.dto.board.BoardSaveRequest;
import project.terminalv2.dto.board.BoardUpdRequest;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ApiResponse;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.AttachedFileRepository;
import project.terminalv2.respository.BoardRepository;
import project.terminalv2.respository.BoardSearchRepository;
import project.terminalv2.util.JwtManager;
import project.terminalv2.vo.board.BoardDetailVo;
import project.terminalv2.vo.board.BoardListVo;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final JwtManager jwtManager;
    private final UserService userService;
    private final ApiResponse apiResponse;
    private final BoardSearchRepository boardSearchRepository;
    private final AttachedFileRepository attachedFileRepository;

    @Transactional
    public ApiResponse saveBoard(BoardSaveRequest request, HttpServletRequest tokenInfo) {

        String token = tokenInfo.getHeader("jwt");
        String userId = jwtManager.getSubject(token);

        Board board = Board.builder()
                .title(request.getTitle())
                .boardType(BoardType.ofCode(request.getBoardTypeCode()))
                .writer(userId)
                .content(request.getContent())
                .build();

        boardRepository.save(board);
        BoardDetailVo boardDetailVo = board.toBoardDetailVo(board);

        return apiResponse.makeResponse(HttpStatus.OK, "2000", "게시글 저장 성공", boardDetailVo);
    }

    @Transactional
    public ApiResponse getBoardInfoOne(Long boardNo) {

        // 게시글 검색
        // 해당 게시글이 없으면 예외처리
        Board board = getBoard(boardNo);
        BoardDetailVo boardDetailVo = board.toBoardDetailVo(board);

        return apiResponse.makeResponse(HttpStatus.OK, "2000", "개별 게시판 조회 성공", boardDetailVo);
    }

    @Transactional
    public ApiResponse getBoardList(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardNo"));
        Page<Board> boards = boardRepository.findAll(pageable);
        Page<BoardListVo> boardInfoVos = boards.map(board -> BoardListVo.builder()
                .boardNo(board.getBoardNo())
                .boardType(board.getBoardType())
                .title(board.getTitle())
                .createdDate(board.getCreatedDate())
                .writer(board.getWriter())
                .build());

        return apiResponse.makeResponse(HttpStatus.OK, "2000", "게시판 목록 조회 성공", boardInfoVos);
    }

    @Transactional
    public ApiResponse updateBoard(Long boardNo, BoardUpdRequest request, HttpServletRequest tokenInfo) {

        Board board = getBoard(boardNo);

        if (userService.hasAccessAuth(board.getWriter(), tokenInfo)) {
            board.update(request);
            BoardDetailVo boardDetailVo = board.toBoardDetailVo(board);
            return apiResponse.makeResponse(HttpStatus.OK, "2000", "게시글 수정 성공", boardDetailVo);
        } else {
            throw new ApiException(ErrorCode.BOARD_UNAUTHORIZED);
        }
    }

    @Transactional
    public ApiResponse deleteBoard(Long boardNo, HttpServletRequest tokenInfo) {

        Board board = getBoard(boardNo);

        if (userService.hasAccessAuth(board.getWriter(), tokenInfo)) {
            boardRepository.deleteById(boardNo);

            return apiResponse.makeResponse(HttpStatus.OK, "2000", "게시글 삭제 성공", null);
        } else {
            throw new ApiException(ErrorCode.BOARD_UNAUTHORIZED);
        }
    }

    public Board getBoard(Long boardNo) {
        return boardRepository.findById(boardNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BOARD));
    }

    @Transactional
    public ApiResponse searchBoard(LocalDate startDate, LocalDate endDate, Integer page, Integer size, String keyword, SearchType searchType, BoardType boardType) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardNo"));

        // QueryDsl로 카테고리별 검색 기능 생성
        List<Board> boardList = boardSearchRepository.findBySearch(startDate, endDate, page, size, keyword, searchType, boardType);

        // Board -> BoardListVo
        List<BoardListVo> boardListVos = boardList.stream()
                .map(board -> new BoardListVo(board))
                .collect(Collectors.toList());

        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > boardListVos.size() ? boardListVos.size() : (start + pageable.getPageSize());

        // list => page
        Page<BoardListVo> boardListVoPage = new PageImpl<>(boardListVos.subList(start, end), pageable, boardListVos.size());

        return apiResponse.makeResponse(HttpStatus.OK, "2000", "게시글 리스트 조회 성공", boardListVoPage);
    }
}
