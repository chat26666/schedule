package com.example.schedule.dto;
import java.time.LocalDateTime;

public class CommentResponseDto {
    private Long commentId;
    private Long userId;
    private String name;
    private String mention;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
