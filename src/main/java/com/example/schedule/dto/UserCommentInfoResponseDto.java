package com.example.schedule.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserCommentInfoResponseDto {
    private String name;
    private String email;
    private String createdAt;
    private String updatedAt;
    private List<CommentResponseDto> comment = new ArrayList<>();
}
