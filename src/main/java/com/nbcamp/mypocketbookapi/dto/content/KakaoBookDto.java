package com.nbcamp.mypocketbookapi.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

@Getter
public class KakaoBookDto {

    private String isbn;
    private String title;
    private String thumbnail;
    @JsonSetter("url")
    private String bookLink;
    @JsonSetter("contents")
    private String summary;
    @JsonSetter("sale_price")
    private Integer salePrice;
    private String status;

}
