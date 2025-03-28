package com.example.schedule.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class ScheduleFindRequestDto {

    @Range(min = 1, max = 10, message = "1~10 사이의 범위를 지정해주세요")
    private int page;
    @Range(min = 1, max = 10, message = "1~10 사이의 범위를 지정해주세요")
    private int size;
}
