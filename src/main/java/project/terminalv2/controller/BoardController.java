package project.terminalv2.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import project.terminalv2.domain.type.BoardType;
import project.terminalv2.domain.type.SearchType;
import project.terminalv2.dto.board.BoardSaveRequest;
import project.terminalv2.dto.board.BoardUpdRequest;
import project.terminalv2.exception.ApiResponse;
import project.terminalv2.respository.BoardSearchRepository;
import project.terminalv2.service.BoardService;
import project.terminalv2.vo.board.BoardListVo;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class BoardController {

    private final BoardService boardService;

    @ApiOperation(value = "개별 게시글 상세 조회", notes = "개별 게시물을 상세 조회합니다.")
    @GetMapping("/api/v1/board/{boardNo}")
    public ApiResponse getBoardInfoOne(@PathVariable Long boardNo) {
        return boardService.getBoardInfoOne(boardNo);
    }

    @ApiOperation(value = "게시글 목록 조회", notes = "글 목록을 조회합니다.")
    @GetMapping("/api/v1/board/list")
    public ApiResponse getBoardList(@RequestParam Integer page, @RequestParam Integer size) {
        return boardService.getBoardList(page, size);
    }

    // 본인 확인 필요
    @ApiOperation(value = "게시글 생성", notes = "게시글을 생성합니다.")
    @PostMapping("/api/v1/board")
    public ApiResponse saveBoard(@RequestBody BoardSaveRequest request, HttpServletRequest tokenInfo) {
        return boardService.saveBoard(request, tokenInfo);
    }

    // 본인 확인 필요
    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정합니다.")
    @PutMapping("/api/v1/board/{boardNo}")
    public ApiResponse updateBoard(@PathVariable Long boardNo, @RequestBody BoardUpdRequest request, HttpServletRequest tokenInfo) {
        return boardService.updateBoard(boardNo, request, tokenInfo);
    }

    // 본인 확인 필요
    @ApiOperation(value = "게시글 삭제", notes = " 게시글을 삭제합니다.")
    @DeleteMapping("/api/v1/board/{boardNo}")
    public ApiResponse deleteBoard(@PathVariable Long boardNo, HttpServletRequest tokenInfo) {
        return boardService.deleteBoard(boardNo, tokenInfo);
    }

    @ApiOperation(value = "게시글 검색", notes = "게시물을 검색합니다.")
    @GetMapping("/api/v1/board/search")
    public ApiResponse searchBoard(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) SearchType searchType,
                                   @RequestParam BoardType boardType) {
        return boardService.searchBoard(startDate, endDate, page, size, keyword, searchType, boardType);
    }
}
