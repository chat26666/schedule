package com.example.schedule.controller;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.UserAuthRequestDto;
import com.example.schedule.dto.UserCommentInfoResponseDto;
import com.example.schedule.dto.UserInfoResponseDto;
import com.example.schedule.dto.converter.RequestConverter;
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
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;

import static com.example.schedule.util.SessionHelper.getUserId;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final CommonEntityService commonService;
    private final ScheduleReadService readService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createUser(
            @RequestBody @Validated UserSaveRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commonService.createUser(dto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @RequestBody @Validated UserAuthRequestDto dto,
            @PathVariable @Min(value = 1, message = "유저 ID 최소값은 1 이상이어야 합니다") Long userId,
            HttpSession session) {
        dto.setUserId(userId);
        readService.validatePassword(dto);
        commonService.deleteUser(userId, SessionHelper.getUserId(session));
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDto> modifyUser(
            @RequestBody @Validated UserSaveRequestDto dto,
            @PathVariable @Min(value = 1, message = "유저 ID 최소값은 1 이상이어야 합니다") Long userId,
            HttpSession session) {
        readService.validatePassword(RequestConverter.convertToUserAuthRequest(dto, userId));
        commonService.modifyUser(dto, userId, SessionHelper.getUserId(session));
        return ResponseEntity.status(HttpStatus.OK).body(readService.findUser(userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDto> findUser(
            @PathVariable @Min(value = 1, message = "유저 ID 최소값은 1 이상이어야 합니다") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(readService.findUser(userId));
    }

    @GetMapping("/{userId}/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> findScheduleAll(
            @PathVariable @Min(value = 1, message = "유저 ID 최소값은 1 이상이어야 합니다") Long userId,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "최소값은 1 이상이어야 합니다") Integer page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "최소값은 1 이상이어야 합니다") Integer size) {
        return ResponseEntity.status(HttpStatus.OK).body(readService.findScheduleAll(userId, page - 1, size));
    }

    @GetMapping("/{userId}/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> findScheduleOne(
            @PathVariable @Min(value = 1, message = "유저 ID 최소값은 1 이상이어야 합니다") Long userId,
            @PathVariable @Min(value = 1, message = "일정 ID 최소값은 1 이상이어야 합니다") Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(readService.findScheduleOne(userId, scheduleId,false));
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<UserCommentInfoResponseDto> findUserComment(
            @PathVariable @Min(value = 1, message = "유저 ID 최소값은 1 이상이어야 합니다") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(readService.findUserComment(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserInfoResponseDto>> findAllUser() {
        return ResponseEntity.status(HttpStatus.OK).body(readService.findAllUser());
    }
}
