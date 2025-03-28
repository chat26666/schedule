package com.example.schedule.service;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.dto.UserAuthRequestDto;
import java.util.Map;

public interface CommonEntityService {

    Map<String, Long> createUser(UserSaveRequestDto dto);
    void authUser(UserAuthRequestDto dto);
    void deleteUser(UserAuthRequestDto dto, Long userId);
}
