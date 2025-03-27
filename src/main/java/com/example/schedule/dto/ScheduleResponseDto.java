package com.example.schedule.dto;
import java.time.LocalDateTime;

public class ScheduleResponseDto {

    long scheduleId;
    String name;
    String title;
    String plan;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    CommentResponseDto comment;
}
