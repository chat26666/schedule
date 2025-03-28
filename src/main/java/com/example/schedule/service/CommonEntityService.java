package com.example.schedule.service;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.ScheduleSaveRequestDto;
import com.example.schedule.dto.UserAuthRequestDto;
import com.example.schedule.dto.UserResponseDto;
import java.util.Map;

public interface CommonEntityService {

    Map<String, Long> createUser(UserSaveRequestDto dto);
    void authUser(UserAuthRequestDto dto);
    void deleteUser(UserAuthRequestDto dto, Long userId);
    UserResponseDto modifyUser(UserSaveRequestDto dto, Long userId);
    ScheduleResponseDto createSchedule(ScheduleSaveRequestDto dto, Long userId);
}
