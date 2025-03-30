package com.example.schedule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserSaveRequestDto {

    @NotBlank(message = "이름은 필수 입력값입니다")
    @Size(max = 40, message = "이름 최대 길이는 40글자 이하로만 가능합니다")
    private String name;

    @NotBlank(message = "이메일은 필수 입력값입니다")
    @Size(max = 70, message = "이메일 최대 길이는 70글자 이하로만 가능합니다")

    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다"
    )
    private String password;
}
