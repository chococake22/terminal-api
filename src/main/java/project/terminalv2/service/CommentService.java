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
import project.terminalv2.dto.CommentSaveRequest;
import project.terminalv2.dto.CommentUpdRequest;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.BoardRepository;
import project.terminalv2.respository.CommentRepository;
import project.terminalv2.vo.CommentInfoVo;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ResponseEntity saveComment(Long boardNo, CommentSaveRequest request) {

        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BOARD));

        Comment comment = Comment.builder()
                .writer("apple")
                .content(request.getContent())
                .board(board)
                .build();

        log.info("댓글 = {}", comment);

        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.OK).body("댓글 작성 성공");
    }

    @Transactional
    public ResponseEntity getCommentList(Long boardNo, Integer page, Integer size) {

        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "commentNo"));

        Page<Comment> comments = commentRepository.findAllByBoardBoardNo(boardNo, pageable);

        Page<CommentInfoVo> commentInfoVos = comments.map(comment -> CommentInfoVo.builder()
                .commentNo(comment.getCommentNo())
                .writer(comment.getWriter())
                .content(comment.getContent())
                .writeDate(comment.getCreatedDate())
                .build());

        return ResponseEntity.status(HttpStatus.OK).body(commentInfoVos);
    }

    @Transactional
    public ResponseEntity updateComment(Long commentNo, CommentUpdRequest request) {

        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_COMMENT));

        comment.update(request);

        return ResponseEntity.status(HttpStatus.OK).body("댓글 수정 성공");
    }

    @Transactional
    public ResponseEntity deleteComment(Long commentNo) {

        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_COMMENT));

        commentRepository.delete(comment);

        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 성공");
    }
}
