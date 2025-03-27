package com.example.schedule.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserLoginRequestDto {
    @NotNull(message = "아이디를 입력해주십시오.")
    private long userId;
    @NotBlank(message = "비밀번호를 입력해주십시오.")
    private String password;
}
