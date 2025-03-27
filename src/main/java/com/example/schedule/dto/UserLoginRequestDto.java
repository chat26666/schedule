package com.example.schedule.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserLoginRequestDto {
    @NotNull(message = "아이디를 입력해주십시오.")
    long userId;
    @NotBlank(message = "비밀번호를 입력해주십시오.")
    String password;
}
