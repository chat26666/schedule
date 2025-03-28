package com.example.schedule.service;
import com.example.schedule.dto.CommentResponseDto;
import com.example.schedule.dto.ScheduleResponseDto;
import java.util.List;

public interface ScheduleJoinQueryService {
    List<CommentResponseDto> findScheduleComment(Long scheduleId);
    List<ScheduleResponseDto> findScheduleAll(Long userId,Integer page,Integer size);
}
