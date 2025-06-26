package com.nbcamp.mypocketbookapi.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WithdrawRequestDto {
    @Schema(description = "이메일", example = "myemail@google.com")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    @NotBlank(message = "이메일은 필수입력사항입니다.")
    private final String email;

    @Schema(description = "비밀번호", example = "mypassword123")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @NotBlank(message = "비밀번호는 필수입력사항입니다.")
    private final String password;
}
