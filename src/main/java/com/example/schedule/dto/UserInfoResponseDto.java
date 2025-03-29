package com.example.schedule.dto;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserInfoResponseDto {
    private String name;
    private String email;
    private String createdAt;
    private String updatedAt;
}
