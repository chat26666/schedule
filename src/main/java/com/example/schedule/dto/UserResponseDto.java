package com.example.schedule.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserResponseDto {
    private String name;
    private String email;
    private String createdAt;
    private String updatedAt;
}
