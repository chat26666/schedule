package com.example.schedule.dto;
import lombok.Getter;
import lombok.Setter;



@Getter
public class UserInfoResponseDto {
    @Setter
    private String name;
    @Setter
    private String email;
    private String createdAt;
    private String updatedAt;
}
