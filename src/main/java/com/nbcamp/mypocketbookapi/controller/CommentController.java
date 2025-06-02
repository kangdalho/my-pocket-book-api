package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.dto.CommentRequest;
import com.nbcamp.mypocketbookapi.entity.Comment;
import com.nbcamp.mypocketbookapi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/reviews/{reviewId}/comments")
    public Comment create(@RequestBody CommentRequest request) {
        return commentService.createComment(request);

    }
}
