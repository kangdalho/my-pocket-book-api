package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.ResponseCode;
import com.nbcamp.mypocketbookapi.dto.content.ContentCreateRequestDto;
import com.nbcamp.mypocketbookapi.dto.content.ContentResponseDto;
import com.nbcamp.mypocketbookapi.dto.content.ContentSearchResponseDto;
import com.nbcamp.mypocketbookapi.security.core.CustomMemberDetails;
import com.nbcamp.mypocketbookapi.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController// 브라우저에서 보내는 요청을 처리하는 클래스
@RequiredArgsConstructor
@Tag(name = "B. 콘텐츠", description = "콘텐츠 관련 API")
public class ContentController {

    private final ContentService contentService;

    // 카카오 api 정보 검색
    @Operation(summary = "카카오 api 검색", description = "카카오 api에서 도서를 검색합니다.")
    @GetMapping("/api/contents/search")
    // @RequestParam String query = query 파라미터를 url에서 받아온다. size - 한번에 검색해서 가져올 결과 개수. 서비스에서 실제 로직처리
    public ResponseEntity<BaseResponse<ContentSearchResponseDto>> searchResponseDto(@RequestParam String query, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_OK, contentService.searchResponseDto(query, size)));
    }

    // 도서 등록
    @Operation(
            summary = "콘텐츠 등록",
            description = "로그인한 회원이 새로운 도서를 등록합니다. 한 회원이 같은 ISBN의 콘텐츠를 중복 등록할 수 없습니다.")
    @PostMapping("/api/contents")
    // 등록이기때문에 defaultvalue가 없어도 된다 size가 의미가없음
    // 회원 id를 url 쿼리 파라미터로 받아옴 서비스에서 실제로직
    public ResponseEntity<BaseResponse<ContentResponseDto>> createContent(@Valid @RequestBody ContentCreateRequestDto requestDto, @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        // 외부 api 에서 검색
        ContentResponseDto savedContent = contentService.createContent(requestDto, customMemberDetails.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(ResponseCode.SUCCESS_CREATED, savedContent));
    }

    // 회원이 등록한 도서 전체 조회
    @Operation(summary = "도서 전체 조회", description = "등록한 도서 전체 조회")
    @GetMapping("/api/contents")
    // List를 page로 변경 <ContentResponseDto>는 콘텐츠 목록을 DTO 형태로 담은 리스트
    public ResponseEntity<BaseResponse<Page<ContentResponseDto>>> findAllContents(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails, @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        // 서비스에 회원 id를 기준으로 컨텐츠 목록 요청
        Page<ContentResponseDto> contents = contentService.findAllContents(customMemberDetails.getMemberId(), pageable);
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_OK, contents));
    }

    // 회원이 등록한 도서 단건 조회
    @Operation(summary = "도서 단건 조회", description = "등록한 도서 단건 조회")
    @GetMapping("/api/contents/{contentId}")
    public ResponseEntity<BaseResponse<ContentResponseDto>> findContentById(@AuthenticationPrincipal CustomMemberDetails customMemberDetails, @PathVariable Long contentId) {
        ContentResponseDto content = contentService.findContentById(customMemberDetails.getMemberId(), contentId);
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_OK, content));
    }

    // 회원이 등록한 도서 삭제
    @Operation(summary = "등록한 도서 삭제", description = "해당 회원이 등록한 도서 삭제 조회")
    @DeleteMapping("/api/contents/{contentId}")
    public ResponseEntity<BaseResponse<Void>> deleteContent(@AuthenticationPrincipal CustomMemberDetails customMemberDetails, @PathVariable Long contentId) {
        contentService.deleteContent(customMemberDetails.getMemberId(), contentId);
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_BOOK_DELETED));
    }

    // 도서 검색 기능
    @Operation(summary = "등록한 도서 검색", description = "summary의 일부분을 입력하여 검색")
    @GetMapping("/api/contents/books/search")
    public ResponseEntity<BaseResponse<Page<ContentResponseDto>>> searchBooks(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
            @RequestParam String keyword,
            @ParameterObject
            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ContentResponseDto> content = contentService.searchContentsBySummary(customMemberDetails.getMemberId(), keyword, pageable);
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_OK, content));


    }
}
