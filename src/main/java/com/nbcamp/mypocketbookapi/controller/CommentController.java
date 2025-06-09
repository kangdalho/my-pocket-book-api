package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.dto.comment.request.CommentRequest;
import com.nbcamp.mypocketbookapi.dto.comment.response.CommentResponse;
import com.nbcamp.mypocketbookapi.entity.Comment;
import com.nbcamp.mypocketbookapi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/reviews/{reviewId}/comments")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest request) {
        Comment comment = commentService.createComment(request);
        CommentResponse response = CommentResponse.fromEntity(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentsByReviewId(@PathVariable Long reviewId) {
        List<CommentResponse> responses = commentService.getCommentsByReviewId(reviewId);
        return ResponseEntity.ok(responses);
    }

        @PutMapping("/api/comments/{commentid}")
        public ResponseEntity<Void> updateComment (
                @PathVariable("commentid") Long id,
                @RequestBody CommentRequest dto
    ){
            commentService.updateComment(id, dto.getText());
            return ResponseEntity.ok().build();
        }

        @DeleteMapping("/api/comments/{commentid}")
        public ResponseEntity<Void> deleteComment (@PathVariable Long id){
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        }
    }


