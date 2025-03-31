package com.example.schedule.controller;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.ScheduleSaveRequestDto;
import com.example.schedule.service.CommonEntityService;
import com.example.schedule.service.ScheduleReadService;
import com.example.schedule.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Min;
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
    private final ScheduleReadService readService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @RequestBody @Validated ScheduleSaveRequestDto dto,
            HttpSession session) {
        Long scheduleId = commonService.createSchedule(dto, SessionHelper.getUserId(session));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(readService.findScheduleOne(SessionHelper.getUserId(session),scheduleId, true));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable @Min(value = 1, message = "일정 ID 최소값은 1 이상이어야 합니다") Long scheduleId,
            HttpSession session) {
        commonService.deleteSchedule(SessionHelper.getUserId(session), scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findScheduleAll(
            HttpSession session,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "최소값은 1 이상이어야 합니다") Integer page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "최소값은 1 이상이어야 합니다") Integer size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(readService.findScheduleAll(SessionHelper.getUserId(session), page - 1, size));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> findScheduleOne(
            @PathVariable @Min(value = 1, message = "일정 ID 최소값은 1 이상이어야 합니다") Long scheduleId,
            HttpSession session) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(readService.findScheduleOne(SessionHelper.getUserId(session), scheduleId,true));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> modifySchedule(
            @RequestBody @Validated ScheduleSaveRequestDto dto,
            @PathVariable @Min(value = 1, message = "일정 ID 최소값은 1 이상이어야 합니다") Long scheduleId,
            HttpSession session) {
        commonService.modifySchedule(dto, SessionHelper.getUserId(session), scheduleId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(readService.findScheduleOne(SessionHelper.getUserId(session), scheduleId, true));
    }
}
