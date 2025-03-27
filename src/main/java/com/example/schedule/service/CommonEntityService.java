package com.example.schedule.service;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.dto.UserLoginRequestDto;
import java.util.Map;

public interface CommonEntityService {

    Map<String, Long> createUser(UserSaveRequestDto dto);
    void loginUser(UserLoginRequestDto dto);
}
