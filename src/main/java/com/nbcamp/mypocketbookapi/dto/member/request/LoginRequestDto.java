package com.nbcamp.mypocketbookapi.dto.member.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    @NotBlank(message = "이메일은 필수입력사항입니다.")
    private String email;

    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @NotBlank(message = "비밀번호는 필수입력사항입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수입력사항입니다.")
    private String nickname;

    @JsonCreator
    public LoginRequestDto(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("nickname") String nickname
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}