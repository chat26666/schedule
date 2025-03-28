package com.example.schedule.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class ScheduleSaveRequestDto {
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(max = 40, message = "제목은 최대 40글자가 넘지 않도록 해주세요.")
    private String title;
    @Size(max = 200, message = "일정은 최대 200글자가 넘지 않도록 해주세요.")
    @NotBlank(message = "일정은 필수 입력값입니다.")
    private String plan;
}
