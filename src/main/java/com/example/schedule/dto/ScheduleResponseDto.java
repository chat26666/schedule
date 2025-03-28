package com.example.schedule.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class ScheduleResponseDto {

    private long scheduleId;
    private String name;
    private String title;
    private String plan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CommentResponseDto comment;
}
