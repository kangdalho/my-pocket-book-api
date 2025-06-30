package com.nbcamp.mypocketbookapi.dto.content;

import lombok.Getter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
// 가독성 높이려고 사용
public class ContentSearchResponseDto {
    List<KakaoBookDto> documents;

}
