package com.example.schedule.service;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.dto.*;

import java.util.Map;

public interface CommonEntityService {

    Map<String, Long> createUser(UserSaveRequestDto dto);
    void authUser(UserAuthRequestDto dto);
    void deleteUser(UserAuthRequestDto dto, Long userId);
    UserResponseDto modifyUser(UserSaveRequestDto dto, Long userId);
    UserResponseDto findUser(Long userId);
    ScheduleResponseDto createSchedule(ScheduleSaveRequestDto dto, Long userId);
    void deleteSchedule(Long userId, Long scheduleId);
    CommentResponseDto createComment(CommentSaveRequestDto dto, Long userId, Long scheduleId);
    void deleteComment(Long userId, Long scheduleId, Long commentId);
    CommentResponseDto modifyComment(CommentSaveRequestDto dto, Long userId, Long scheduleId, Long commentId);
    ScheduleResponseDto modifySchedule(ScheduleSaveRequestDto dto, Long userId, Long scheduleId);
}
