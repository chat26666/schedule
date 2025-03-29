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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Map<String,Object>> createUser(@RequestBody @Validated UserSaveRequestDto dto) {
        return new ResponseEntity(commonService.createUser(dto), HttpStatus.CREATED);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@RequestBody @Validated UserAuthRequestDto dto, @PathVariable Long userId , HttpSession session) {
        if (!SessionHelper.isUserAuthorized(session, userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        //나중에 예외 처리 수정 필요
        dto.setUserId(userId);
        commonService.deleteUser(dto, userId);
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDto> modifyUser(@RequestBody @Validated UserSaveRequestDto dto, @PathVariable Long userId , HttpSession session) {
        if (!SessionHelper.isUserAuthorized(session, userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        //나중에 예외 처리 수정 필요
        commonService.authUser(RequestConverter.convertToUserAuthRequest(dto, userId));
        return new ResponseEntity<>(commonService.modifyUser(dto, userId),HttpStatus.OK);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDto> findUser(@PathVariable Long userId) {
        return new ResponseEntity<>(commonService.findUser(userId),HttpStatus.OK);
    }
    @GetMapping("/{userId}/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> findScheduleAll(@PathVariable Long userId, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.status(HttpStatus.OK).body(joinService.findScheduleAll(userId,page,size));
    }
    @GetMapping("/{userId}/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> findScheduleOne(@PathVariable Long userId, @PathVariable Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(joinService.findScheduleOne(userId,scheduleId));
    }
    @GetMapping("/{userId}/comments")
    public ResponseEntity<UserCommentInfoResponseDto> findUserComment(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(joinService.findUserComment(userId));
    }
}
