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

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final CommonEntityService commonService;
    private final ScheduleReadService joinService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(
            @RequestBody @Validated UserSaveRequestDto dto) {
        return new ResponseEntity(commonService.createUser(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @RequestBody @Validated UserAuthRequestDto dto,
            @PathVariable @Min(value = 1, message = "유저 ID 최소값은 1 이상이어야 합니다") Long userId,
            HttpSession session) {
        if (!SessionHelper.isUserAuthorized(session, userId) && commonService.findUser(userId) != null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "userId : 해당 계정의 변경 권한이 없습니다");
        dto.setUserId(userId);
        commonService.deleteUser(dto, userId);
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDto> modifyUser(
            @RequestBody @Validated UserSaveRequestDto dto,
            @PathVariable @Min(value = 1, message = "유저 ID 최소값은 1 이상이어야 합니다") Long userId,
            HttpSession session) {
        if (!SessionHelper.isUserAuthorized(session, userId) && commonService.findUser(userId) != null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "userId : 해당 계정의 변경 권한이 없습니다");
        commonService.authUser(RequestConverter.convertToUserAuthRequest(dto, userId));
        return new ResponseEntity<>(commonService.modifyUser(dto, userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDto> findUser(
            @PathVariable @Min(value = 1, message = "유저 ID 최소값은 1 이상이어야 합니다") Long userId) {
        return new ResponseEntity<>(commonService.findUser(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> findScheduleAll(
            @PathVariable @Min(value = 1, message = "유저 ID 최소값은 1 이상이어야 합니다") Long userId,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "최소값은 1 이상이어야 합니다") Integer page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "최소값은 1 이상이어야 합니다") Integer size) {
        return ResponseEntity.status(HttpStatus.OK).body(joinService.findScheduleAll(userId, page - 1, size));
    }

    @GetMapping("/{userId}/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> findScheduleOne(
            @PathVariable @Min(value = 1, message = "유저 ID 최소값은 1 이상이어야 합니다") Long userId,
            @PathVariable @Min(value = 1, message = "일정 ID 최소값은 1 이상이어야 합니다") Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(joinService.findScheduleOne(userId, scheduleId,false));
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<UserCommentInfoResponseDto> findUserComment(
            @PathVariable @Min(value = 1, message = "유저 ID 최소값은 1 이상이어야 합니다") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(joinService.findUserComment(userId));
    }
}
