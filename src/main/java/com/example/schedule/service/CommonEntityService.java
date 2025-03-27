package com.example.schedule.service;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.ScheduleSaveRequestDto;

import java.util.HashMap;
import java.util.Map;

public interface CommonEntityService {

    Map<String, Long> createUser(UserSaveRequestDto dto);
    //ScheduleResponseDto createSchedule(ScheduleSaveRequestDto dto);

}
