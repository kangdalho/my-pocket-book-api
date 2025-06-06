package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.dto.content.ContentCreateRequestDto;
import com.nbcamp.mypocketbookapi.dto.content.ContentResponseDto;
import com.nbcamp.mypocketbookapi.dto.content.ContentSearchResponseDto;
import com.nbcamp.mypocketbookapi.service.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController// 브라우저에서 보내는 요청을 처리하는 클래스
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    // 카카오 api 정보 검색
    @GetMapping("/api/contents/search")
    // @RequestParam String query = query 파라미터를 url에서 받아온다. size - 한번에 검색해서 가져올 결과 개수. 서비스에서 실제 로직처리
    public ContentSearchResponseDto searchResponseDto(@RequestParam String query, @RequestParam(defaultValue = "10") int size) {
        return contentService.searchResponseDto(query, size);
    }

    // 도서 등록
    @PostMapping("/api/contents")
    // 등록이기때문에 defaultvalue가 없어도 된다 size가 의미가없음
    // 회원 id를 url 쿼리 파라미터로 받아옴 서비스에서 실제로직
    public ResponseEntity<ContentResponseDto> createContent(@Valid @RequestBody ContentCreateRequestDto requestDto, @RequestParam Long memberId) {
       // 외부 api 에서 검색
        ContentResponseDto savedContent = contentService.createContent(requestDto, memberId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedContent);
    }

    // 회원이 등록한 도서 전체 조회
    @GetMapping("/api/contents")
    // List<ContentResponseDto>는 콘텐츠 목록을 DTO 형태로 담은 리스트
    public ResponseEntity<List<ContentResponseDto>> findAllContents() {
        Long memberId = 1L;
        // 서비스에 회원 id를 기준으로 컨텐츠 목록 요청
        List<ContentResponseDto> contents = contentService.findAllContents(memberId);
        return ResponseEntity.ok(contents);
    }

    // 회원이 등록한 도서 단건 조회
    @GetMapping("/api/contents/{contentId}")
    public ResponseEntity<ContentResponseDto> findContentById(@PathVariable Long contentId) {
        Long memberId = 1L;
        ContentResponseDto content = contentService.findContentById(memberId, contentId);
        return ResponseEntity.ok(content);
    }

    // 회원이 등록한 도서 삭제
    @DeleteMapping("/api/contents/{contentId}")
    public ResponseEntity<String> deleteContent(@PathVariable Long contentId) {
        Long memberId = 1L;
        contentService.deleteContent(memberId, contentId);
        return ResponseEntity.ok("삭제됬습니다.");
    }
}
