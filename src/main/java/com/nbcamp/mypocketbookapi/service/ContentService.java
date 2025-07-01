package com.nbcamp.mypocketbookapi.service;

import com.nbcamp.mypocketbookapi.dto.content.ContentCreateRequestDto;
import com.nbcamp.mypocketbookapi.dto.content.ContentResponseDto;
import com.nbcamp.mypocketbookapi.dto.content.ContentSearchResponseDto;
import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.exception.BusinessException;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;
import com.nbcamp.mypocketbookapi.exception.content.ContentException;
import com.nbcamp.mypocketbookapi.repository.ContentJpaRepository;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final RestClient restClient;
    private final ContentJpaRepository contentJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    // 컨텐츠 검색 조회
    // 외부 검색 api를 호출하고 결과를 dto로 반환하는 메서드
    public ContentSearchResponseDto searchResponseDto(String query, int size) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", query)
                        .queryParam("size", size)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new ContentException(ErrorCode.KAKAO_API_BAD_REQUEST);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new ContentException(ErrorCode.KAKAO_API_INTERNAL_SERVER_ERROR);
                })
                .body(ContentSearchResponseDto.class);
    }

    // 컨텐츠 등록
    @Transactional
    public ContentResponseDto createContent(ContentCreateRequestDto requestDto, Long memberId) {

        // 회원 조회
        // 멤버id로 회원 조회 존재하지않으면 예외발생
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(()-> new ContentException(ErrorCode.CONTENT_NOT_FOUND));

//        // ISBN 중복 체크 이미등록된 경우 예외발생
//        if(contentJpaRepository.existsByIsbn(requestDto.getIsbn())) {
//            throw new BusinessException(ErrorCode.DUPLICATE_CONTENT);
//        }

        // ISBN 중복체크(같은 회원이 동일한 ISBN으로 등록 하는 경우만 방지)
        if(contentJpaRepository.existsByMemberAndIsbn(member, requestDto.getIsbn())) {
            throw new ContentException(ErrorCode.DUPLICATE_CONTENT);
        }

        // Content 객체 생성
        Content content = new Content(
                member,
                requestDto.getIsbn(),
                requestDto.getTitle(),
                requestDto.getThumbnail(),
                requestDto.getBookLink(),
                requestDto.getSummary(),
                requestDto.getSalePrice(),
                requestDto.getStatus()
        );
        // 생성된 엔티티를 데이터베이스에 저장
        Content saved = contentJpaRepository.save(content);

        //  저장 및 반환
        return new ContentResponseDto(saved);

    }

    // id 기준 등록한 도서 전체 조회
    @Transactional(readOnly = true)
    public Page<ContentResponseDto> findAllContents(Long memberId, Pageable pageable) {

        // 해당 id 회원 조회 없으면 예외발생
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(()-> new ContentException(ErrorCode.MEMBER_NOT_FOUND));

        // 해당 회원이 등록한 모든 콘텐츠 조회
        Page<Content> contentPage = contentJpaRepository.findByMember(member, pageable);

        return contentPage.map(content -> new ContentResponseDto(content.getId(),
                        content.getIsbn(),
                        content.getTitle(),
                        content.getThumbnail(),
                        content.getBookLink(),
                        content.getSummary(),
                        content.getSalePrice(),
                        content.getStatus(),
                        content.getCreatedAt()
        ));
    }

    // 단건 조회
    // 특정회원(memberId)가 등록한 contentId에 해당하는 콘텐츠 조회
    @Transactional(readOnly = true)
    public ContentResponseDto findContentById(Long memberId, Long contentId) {
        // contentId, memberId 가 모두 일치하는 콘텐츠 조회 없으면 예외발생
        Content content = contentJpaRepository.findByIdAndMemberId(contentId, memberId)
                .orElseThrow(() -> new ContentException(ErrorCode.CONTENT_NOT_FOUND));
        return new ContentResponseDto(content);
    }


    // 삭제
    // 결과를 반환하지 않아도 되기때문에 void
    @Transactional
    public void deleteContent(Long memberId, Long contentId) {
        //해당 contentId로 콘텐츠 조회
        Content content = contentJpaRepository.findById(contentId)
                .orElseThrow(()-> new ContentException(ErrorCode.CONTENT_NOT_FOUND));
        // 해당 콘텐츠의 작성자 id와 현재 요청한 id가 같은지 비교 다르면 예외발생
        if(!content.getMember().getId().equals(memberId)) {
            throw new ContentException(ErrorCode.UNAUTHORIZED_CONTENT_DELETION);
        }
        // 다 통과했으면 삭제
        contentJpaRepository.delete(content);

    }

    // 도서 검색 기능
    public Page<ContentResponseDto> searchContentsBySummary(Long memberId, String keyword, Pageable pageable) {
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(()-> new ContentException(ErrorCode.MEMBER_NOT_FOUND));


//        Page<Content> contents = contentJpaRepository.findByMemberAndSummaryContainingIgnoreCase(member, keyword, pageable);

        Page<Content> contents = contentJpaRepository.searchBySummaryFullText(memberId, keyword, pageable);

        return contents.map(content -> new ContentResponseDto(
                content.getId(),
                content.getIsbn(),
                content.getTitle(),
                content.getThumbnail(),
                content.getBookLink(),
                content.getSummary(),
                content.getSalePrice(),
                content.getStatus(),
                content.getCreatedAt()
        ));

    }


}
