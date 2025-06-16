package com.nbcamp.mypocketbookapi.dto.content;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ContentCreateRequestDto {

    @NotBlank(message = "필수 입력값입니다.")
    private String isbn;
    @NotBlank(message = "필수 입력값입니다.")
    private String title;
    @NotBlank(message = "필수 입력값입니다.")
    @Size(max = 512, message = "썸네일은 512자 이하여야 합니다")
    private String thumbnail;
    @NotBlank(message = "필수 입력값입니다.")
    @Size(max = 512, message = "북링크는 512자 이하여야 합니다")
    private String bookLink;
    @NotBlank(message = "필수 입력값입니다.")
    private String summary;
    @NotBlank(message = "필수 입력값입니다.")
    private Integer salePrice;
    @NotBlank(message = "필수 입력값입니다.")
    @Size(max = 10, message = "status는 10자 이하여야 합니다")
    private String status;
}
