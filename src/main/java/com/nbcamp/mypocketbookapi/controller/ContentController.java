package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.dto.ContentCreateRequestDto;
import com.nbcamp.mypocketbookapi.dto.ContentCreateResponseDto;
import com.nbcamp.mypocketbookapi.dto.ContentSearchResponseDto;
import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController// 브라우저에서 보내는 요청을 처리하는 클래스
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    // 카카오 api 정보 검색
    @GetMapping("/api/contents/search")
    public ContentSearchResponseDto searchResponseDto(@RequestParam String query, @RequestParam(defaultValue = "10") int size) {
        return contentService.searchResponseDto(query, size);
    }

    // 도서 등록
    @PostMapping("/api/contents")
    // 등록이기때문에 defaultvalue가 없어도 된다 size가 의미가없음
    public ResponseEntity<ContentCreateResponseDto> createContent(@RequestBody ContentCreateRequestDto requestDto, @RequestParam Long memberId) {
       // 외부 api 에서 검색
        ContentCreateResponseDto savedContent = contentService.createContent(requestDto, memberId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedContent);


    }
}
