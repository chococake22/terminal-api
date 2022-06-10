package project.terminalv2.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.terminalv2.dto.comment.CommentSaveRequest;
import project.terminalv2.dto.comment.CommentUpdRequest;
import project.terminalv2.service.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 작성", notes = "댓글을 작성합니다.")
    @PostMapping("/api/v1/{boardNo}/comment")
    public ResponseEntity saveComment(@PathVariable Long boardNo, @RequestBody CommentSaveRequest request) {
        return commentService.saveComment(boardNo, request);
    }

    @ApiOperation(value = "댓글 목록 조회", notes = "댓글 목록을 조회합니다.")
    @GetMapping("/api/v1/{boardNo}/comments")
    public ResponseEntity getCommentList(@PathVariable Long boardNo, @RequestParam Integer page, @RequestParam Integer size) {
        return commentService.getCommentList(boardNo, page, size);

    }

    @ApiOperation(value = "댓글 수정", notes = "댓글을 수정합니다.")
    @PutMapping("/api/v1/comment/{commentNo}")
    public ResponseEntity updateComment(@PathVariable Long commentNo, @RequestBody CommentUpdRequest request) {
        return commentService.updateComment(commentNo, request);
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제합니다.")
    @DeleteMapping("/api/v1/comment/{commentNo}")
    public ResponseEntity deleteComment(@PathVariable Long commentNo) {
        return commentService.deleteComment(commentNo);
    }

}
