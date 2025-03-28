package com.example.schedule.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserAuthRequestDto {
    @NotNull(message = "아이디를 입력해주십시오.", groups = AuthLogin.class)
    private long userId;
    @NotBlank(message = "비밀번호를 입력해주십시오.")
    private String password;
}
