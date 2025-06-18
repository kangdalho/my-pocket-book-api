package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.common.ResponseCode;
import com.nbcamp.mypocketbookapi.dto.content.ContentCreateRequestDto;
import com.nbcamp.mypocketbookapi.dto.content.ContentResponseDto;
import com.nbcamp.mypocketbookapi.dto.content.ContentSearchResponseDto;
import com.nbcamp.mypocketbookapi.service.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController// 브라우저에서 보내는 요청을 처리하는 클래스
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    // 카카오 api 정보 검색
    @GetMapping("/api/contents/search")
    // @RequestParam String query = query 파라미터를 url에서 받아온다. size - 한번에 검색해서 가져올 결과 개수. 서비스에서 실제 로직처리
    public ResponseEntity<BaseResponse<ContentSearchResponseDto>> searchResponseDto(@RequestParam String query, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_OK, contentService.searchResponseDto(query, size)));
    }

    // 도서 등록
    @PostMapping("/api/contents")
    // 등록이기때문에 defaultvalue가 없어도 된다 size가 의미가없음
    // 회원 id를 url 쿼리 파라미터로 받아옴 서비스에서 실제로직
    public ResponseEntity<BaseResponse<ContentResponseDto>> createContent(@Valid @RequestBody ContentCreateRequestDto requestDto, @LoginMember Long memberId) {
       // 외부 api 에서 검색
        ContentResponseDto savedContent = contentService.createContent(requestDto, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(ResponseCode.SUCCESS_CREATED, savedContent));
    }

    // 회원이 등록한 도서 전체 조회
    @GetMapping("/api/contents")
    // List를 page로 변경 <ContentResponseDto>는 콘텐츠 목록을 DTO 형태로 담은 리스트
    public ResponseEntity<BaseResponse<Page<ContentResponseDto>>> findAllContents(
            @LoginMember Long memberId, @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        // 서비스에 회원 id를 기준으로 컨텐츠 목록 요청
        Page<ContentResponseDto> contents = contentService.findAllContents(memberId, pageable);
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_OK, contents));
    }

    // 회원이 등록한 도서 단건 조회
    @GetMapping("/api/contents/{contentId}")
    public ResponseEntity<BaseResponse<ContentResponseDto>> findContentById(@PathVariable Long contentId, @LoginMember Long memberId) {
        ContentResponseDto content = contentService.findContentById(memberId, contentId);
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_OK, content));
    }

    // 회원이 등록한 도서 삭제
    @DeleteMapping("/api/contents/{contentId}")
    public ResponseEntity<BaseResponse<Void>> deleteContent(@PathVariable Long contentId, @LoginMember Long memberId) {
        contentService.deleteContent(memberId, contentId);
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_BOOK_DELETED));
    }

    // 검색
}
