package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.dto.ContentSearchResponseDto;
import com.nbcamp.mypocketbookapi.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController// 브라우저에서 보내는 요청을 처리하는 클래스
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/api/contents/search")
    public ContentSearchResponseDto searchResponseDto(@RequestParam String query, @RequestParam(defaultValue = "10")int size) {
        return contentService.searchResponseDto(query, size);
    }
}
