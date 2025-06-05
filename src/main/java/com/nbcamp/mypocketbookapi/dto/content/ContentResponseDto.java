package com.nbcamp.mypocketbookapi.dto.content;

import com.nbcamp.mypocketbookapi.entity.Content;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ContentResponseDto {

    private Long id;
    private String isbn;
    private String title;
    private String thumbnail;
    private String bookLink;
    private String summary;
    private Integer salePrice;
    private String status;
    private LocalDateTime createdAt;

    public ContentResponseDto(Content content) {
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


    public ContentResponseDto(Long id, String isbn, String title, String thumbnail, String bookLink, String summary, Integer salePrice, String status, LocalDateTime createdAt) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.thumbnail = thumbnail;
        this.bookLink = bookLink;
        this.summary = summary;
        this.salePrice = salePrice;
        this.status = status;
        this.createdAt = createdAt;
    }
}
