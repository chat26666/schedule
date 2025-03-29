package com.example.schedule.controller;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.ScheduleSaveRequestDto;
import com.example.schedule.service.CommonEntityService;
import com.example.schedule.service.ScheduleReadService;
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
    private final ScheduleReadService joinService;

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
    public ResponseEntity<List<ScheduleResponseDto>> findScheduleAll(HttpSession session, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.status(HttpStatus.OK).body(joinService.findScheduleAll(SessionHelper.getUserId(session),page,size));
    }
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> findScheduleOne(@PathVariable Long scheduleId , HttpSession session) {
        return ResponseEntity.status(HttpStatus.OK).body(joinService.findScheduleOne(SessionHelper.getUserId(session),scheduleId));
    }
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> modifySchedule(@RequestBody @Validated ScheduleSaveRequestDto dto, @PathVariable Long scheduleId, HttpSession session) {
        return ResponseEntity.status(HttpStatus.OK).body(commonService.modifySchedule(dto,SessionHelper.getUserId(session),scheduleId));
    }
}
