package com.nbcamp.mypocketbookapi.dto;

import com.nbcamp.mypocketbookapi.entity.Content;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
public class ContentCreateResponseDto {

    private Long id;
    private String isbn;
    private String title;
    private String thumbnail;
    private String bookLink;
    private String summary;
    private Integer salePrice;
    private String status;
    private LocalDateTime createdAt;

    public ContentCreateResponseDto(Content content) {
        this.id = content.getId();
        this.isbn =content.getIsbn();
        this.title = content.getTitle();
        this.thumbnail = content.getThumbnail();
        this.bookLink = content.getBookLink();
        this.summary = content.getSummary();
        this.salePrice = content.getSalePrice();
        this.status = content.getStatus();
        this.createdAt = content.getCreatedAt();
    }

}
