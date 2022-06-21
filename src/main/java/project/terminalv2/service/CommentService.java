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
import project.terminalv2.domain.Comment;
import project.terminalv2.dto.comment.CommentSaveRequest;
import project.terminalv2.dto.comment.CommentUpdRequest;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ApiResponse;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.BoardRepository;
import project.terminalv2.respository.CommentRepository;
import project.terminalv2.vo.comment.CommentInfoVo;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final ApiResponse apiResponse;

    @Transactional
    public ApiResponse saveComment(Long boardNo, CommentSaveRequest request, HttpServletRequest tokenInfo) {

        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BOARD));

        String token = tokenInfo.getHeader("jwt");
        String userId = jwtService.getSubject(token);

        Comment comment = Comment.builder()
                .writer(userId)
                .content(request.getContent())
                .boardNo(boardNo)
                .build();

        commentRepository.save(comment);

        CommentInfoVo commentInfoVo = CommentInfoVo.builder()
                .commentNo(comment.getCommentNo())
                .writer(comment.getWriter())
                .content(comment.getContent())
                .build();

        return apiResponse.makeResponse(HttpStatus.OK, "3000", "댓글 저장 성공", commentInfoVo);
    }

    @Transactional
    public ApiResponse getCommentList(Long boardNo, Integer page, Integer size) {

        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "commentNo"));

        Page<Comment> comments = commentRepository.findAllByBoardNo(boardNo, pageable);

        Page<CommentInfoVo> commentInfoVos = comments.map(comment -> CommentInfoVo.builder()
                .commentNo(comment.getCommentNo())
                .writer(comment.getWriter())
                .content(comment.getContent())
                .writeDate(comment.getCreatedDate())
                .build());

        return apiResponse.makeResponse(HttpStatus.OK, "3000", "댓글 리스트 조회 성공", commentInfoVos);
    }

    @Transactional
    public ApiResponse updateComment(Long commentNo, CommentUpdRequest request, HttpServletRequest tokenInfo) {

        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_COMMENT));

        String userId = userService.getUserIdFromToken(tokenInfo);

        if(userService.hasAccessAuth(userId, tokenInfo)) {
            comment.update(request);

            CommentInfoVo commentInfoVo = CommentInfoVo.builder()
                    .commentNo(comment.getCommentNo())
                    .writer(comment.getWriter())
                    .content(comment.getContent())
                    .build();

            return apiResponse.makeResponse(HttpStatus.OK, "3000", "댓글 수정 성공", commentInfoVo);
        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }
    }

    @Transactional
    public ApiResponse deleteComment(Long commentNo, HttpServletRequest tokenInfo) {

        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_COMMENT));

        String userId = userService.getUserIdFromToken(tokenInfo);

        if(userService.hasAccessAuth(userId, tokenInfo)) {
            commentRepository.delete(comment);
            return apiResponse.makeResponse(HttpStatus.OK, "3000", "댓글 삭제 성공", null);
        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
