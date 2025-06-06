package com.nbcamp.mypocketbookapi.dto.content;

import lombok.Getter;

@Getter
public class ContentSearchRequestDto {
    // long id는 아직 없기때문에 생략
    private Long memberId;
    private String isbn;
    private String title;
    private String thumbnail;
    private String bookLink;
    private String summary;
    private Integer salePrice;
    private String status;
}
