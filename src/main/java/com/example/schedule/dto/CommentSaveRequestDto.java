package com.example.schedule.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentSaveRequestDto {

    @NotBlank(message = "댓글을 입력해주세요")
    @Size(max = 55, message = "댓글은 55 글자를 넘을 수 없습니다")
    private String mention;
}
