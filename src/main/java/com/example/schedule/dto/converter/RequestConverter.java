package com.example.schedule.dto.converter;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.dto.UserAuthRequestDto;

public class RequestConverter {
    public static UserAuthRequestDto convertToUserAuthRequest(UserSaveRequestDto dto, Long userId) {
        return new UserAuthRequestDto(userId,dto.getPassword());
    }
}
