package com.example.schedule.service;
import com.example.schedule.UserSaveRequestDto;
import java.util.HashMap;
import java.util.Map;

public interface CommonEntityService {

    Map<String, Long> createUser(UserSaveRequestDto dto);

}
