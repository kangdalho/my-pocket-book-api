package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.dto.comment.request.CommentRequest;
import com.nbcamp.mypocketbookapi.dto.comment.response.CommentResponse;
import com.nbcamp.mypocketbookapi.entity.Comment;
import com.nbcamp.mypocketbookapi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/reviews/{reviewId}/comments")
    public Comment create(@RequestBody CommentRequest request) {
        return commentService.createComment(request);

    }

    @GetMapping
    public List<CommentResponse> getAllComments() {
        return commentService.findAllComments();
    }
}
