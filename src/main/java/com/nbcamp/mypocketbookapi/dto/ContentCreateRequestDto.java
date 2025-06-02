package com.nbcamp.mypocketbookapi.dto;

import lombok.Getter;

import java.math.BigDecimal;

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
