package com.example.schedule.service;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.dto.*;

import java.util.Map;

public interface CommonEntityService {

    Map<String, Long> createUser(UserSaveRequestDto dto);
    void deleteUser(Long userId, Long sessionUserId);
    void modifyUser(UserSaveRequestDto dto, Long userId, Long sessionUserId);
    Long createSchedule(ScheduleSaveRequestDto dto, Long userId);
    void deleteSchedule(Long userId, Long scheduleId);
    Long createComment(CommentSaveRequestDto dto, Long userId, Long scheduleId);
    void deleteComment(Long userId, Long scheduleId, Long commentId);
    void modifyComment(CommentSaveRequestDto dto, Long userId, Long scheduleId, Long commentId);
    void modifySchedule(ScheduleSaveRequestDto dto, Long userId, Long scheduleId);
}
