package com.nbcamp.mypocketbookapi.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ContentSearchRequestDto {
    // long id는 아직 없기때문에 생략
    private Long memberId;
    private String isbn;
    private String title;
    private String thumbnail;
    private String bookLink;
    private String summary;
    private BigDecimal salePrice;
    private String status;
}
