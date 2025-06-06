package com.nbcamp.mypocketbookapi.dto.content;

import lombok.Getter;

@Getter
public class ContentCreateRequestDto {

    private String isbn;
    private String title;
    private String thumbnail;
    private String bookLink;
    private String summary;
    private Integer salePrice;
    private String status;
}
