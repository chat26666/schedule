package com.example.schedule.dto;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Range;

@AllArgsConstructor
public class ScheduleFindRequestDto {

    @Range(min = 1, max = 10, message = "1~10 사이의 범위를 지정해주세요")
    int page;
    @Range(min = 1, max = 10, message = "1~10 사이의 범위를 지정해주세요")
    int size;
}
