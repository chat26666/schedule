package com.example.schedule.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
public class ScheduleResponseDto {

    private long scheduleId;
    private String name;
    private String title;
    private String plan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponseDto> comment = new ArrayList<>();
}
