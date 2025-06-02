package com.nbcamp.mypocketbookapi.service;

import com.nbcamp.mypocketbookapi.dto.ContentCreateRequestDto;
import com.nbcamp.mypocketbookapi.dto.ContentCreateResponseDto;
import com.nbcamp.mypocketbookapi.dto.ContentSearchResponseDto;
import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.repository.ContentJpaRepository;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final RestClient restClient;
    private final ContentJpaRepository contentJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    public ContentSearchResponseDto searchResponseDto(String query, int size) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", query)
                        .queryParam("size", size)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "클라이언트 오류 발생: " + response.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 발생: " + response.getStatusCode());
                })
                .body(ContentSearchResponseDto.class);
    }



    public ContentCreateResponseDto createContent(ContentCreateRequestDto requestDto, Long memberId) {

        // 4. 회원 조회
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."));

        // 5. Content 객체 생성
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

        Content saved = contentJpaRepository.save(content);

        // 6. 저장 및 반환
        return new ContentCreateResponseDto(saved);


    }
}
