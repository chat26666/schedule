package com.example.schedule.service;
import com.example.schedule.dto.CommentResponseDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.UserCommentInfoResponseDto;

import java.util.List;

public interface ScheduleReadService {
    List<CommentResponseDto> findScheduleComment(Long scheduleId);
    List<ScheduleResponseDto> findScheduleAll(Long userId,Integer page,Integer size);
    ScheduleResponseDto findScheduleOne(Long userId, Long scheduleId, boolean isMySchedule);
    UserCommentInfoResponseDto findUserComment(Long userId);
}
