package com.example.schedule.dto;
import java.time.LocalDateTime;

public class CommentResponseDto {
    Long commentId;
    Long userId;
    String name;
    String mention;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
