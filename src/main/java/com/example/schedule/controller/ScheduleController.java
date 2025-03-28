package com.example.schedule.controller;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.ScheduleSaveRequestDto;
import com.example.schedule.service.CommonEntityService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
@Validated
@RequiredArgsConstructor
public class ScheduleController {

    private final CommonEntityService commonService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody @Validated ScheduleSaveRequestDto dto, HttpSession session) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commonService.createSchedule(dto,(Long)session.getAttribute("userId")));
    }

}
