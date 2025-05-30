package com.nbcamp.mypocketbookapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class KakaoBookDto {
    private String isbn;
    private String title;
    private String thumbnail;
    @JsonProperty("url")
    private String bookLink;
    @JsonProperty("contents")
    private String summary;
    @JsonProperty("sale_price")
    private BigDecimal salePrice;
    private String status;
}
