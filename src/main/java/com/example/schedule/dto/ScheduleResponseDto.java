package com.example.schedule.dto;
import java.time.LocalDateTime;

public class ScheduleResponseDto {

    private long scheduleId;
    private String name;
    private String title;
    private String plan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CommentResponseDto comment;
}
