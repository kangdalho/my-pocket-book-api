package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.common.ResponseCode;
import com.nbcamp.mypocketbookapi.dto.comment.request.CommentRequest;
import com.nbcamp.mypocketbookapi.dto.comment.response.CommentResponse;
import com.nbcamp.mypocketbookapi.entity.Comment;
import com.nbcamp.mypocketbookapi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/reviews/{reviewId}/comments")
    public ResponseEntity<BaseResponse<CommentResponse>> createComment(@PathVariable Long reviewId, @LoginMember Long memberId, @RequestBody CommentRequest request) {
        Comment comment = commentService.createComment(reviewId, memberId, request);
        CommentResponse response = CommentResponse.fromEntity(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success(ResponseCode.SUCCESS_CREATED, response));
    }

    @GetMapping("/reviews/{reviewId}/comments")
    public ResponseEntity<BaseResponse<Page<CommentResponse>>> getCommentsByReviewId(
            @PathVariable Long reviewId,
            @ParameterObject
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<CommentResponse> responses = commentService.getCommentsByReviewId(reviewId, pageable);

        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_OK, responses));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Void>> updateComment (
                @PathVariable Long commentId,
                @LoginMember Long memberId,
                @RequestBody CommentRequest dto
    ){
            commentService.updateComment(commentId,memberId, dto.getText());
            return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_OK));
        }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Void>> deleteComment (@PathVariable Long commentId, @LoginMember Long memberId){
        commentService.deleteComment(commentId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.success(ResponseCode.SUCCESS_OK));
    }
}
