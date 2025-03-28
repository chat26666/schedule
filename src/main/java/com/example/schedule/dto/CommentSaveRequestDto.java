package com.example.schedule.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class CommentSaveRequestDto {

    @NotBlank(message = "댓글을 입력해주세요.")
    @Size(max = 55, message = "댓글은 30 글자를 넘을 수 없습니다.")
    private String mention;
}
