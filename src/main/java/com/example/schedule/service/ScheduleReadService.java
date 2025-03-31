package com.example.schedule.service;
import com.example.schedule.dto.*;
import com.example.schedule.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScheduleReadService {
    List<CommentResponseDto> findScheduleComment(Long scheduleId);
    List<ScheduleResponseDto> findScheduleAll(Long userId,Integer page,Integer size);
    ScheduleResponseDto findScheduleOne(Long userId, Long scheduleId, boolean isMySchedule);
    UserCommentInfoResponseDto findUserComment(Long userId);
    List<UserInfoResponseDto> findAllUser();
    UserInfoResponseDto findUser(Long userId);
    CommentResponseDto findComment(Long scheduleId, Long commentId);
    void authUser(UserAuthRequestDto dto);
}
