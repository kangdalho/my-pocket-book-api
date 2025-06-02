package com.nbcamp.mypocketbookapi.dto;

import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
// 가독성 높이려고 사용
public class ContentSearchResponseDto {
    List<KakaoBookDto> documents;

}
