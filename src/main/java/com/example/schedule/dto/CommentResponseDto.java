package com.example.schedule.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class CommentResponseDto {
    private Long commentId;
    private Long userId;
    private String name;
    private String mention;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
