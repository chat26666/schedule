package com.example.schedule.controller;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.ScheduleSaveRequestDto;
import com.example.schedule.service.CommonEntityService;
import com.example.schedule.service.ScheduleJoinQueryService;
import com.example.schedule.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@Validated
@RequiredArgsConstructor
public class ScheduleController {

    private final CommonEntityService commonService;
    private final ScheduleJoinQueryService joinService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody @Validated ScheduleSaveRequestDto dto, HttpSession session) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commonService.createSchedule(dto, SessionHelper.getUserId(session)));
    }
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId, HttpSession session) {
        commonService.deleteSchedule(SessionHelper.getUserId(session),scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findMySchedule(HttpSession session) {
        return ResponseEntity.status(HttpStatus.OK).body(joinService.findMySchedule(SessionHelper.getUserId(session)));
    }

}
