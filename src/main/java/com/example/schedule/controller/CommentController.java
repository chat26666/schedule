package com.example.schedule.controller;
import com.example.schedule.dto.CommentResponseDto;
import com.example.schedule.dto.CommentSaveRequestDto;
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
@RequestMapping("/api/schedules/{scheduleId}/comments")
@Validated
@RequiredArgsConstructor
public class CommentController {

    private final CommonEntityService commonService;
    private final ScheduleReadService readService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @RequestBody @Validated CommentSaveRequestDto dto,
            @PathVariable @Min(value = 1, message = "일정 ID 최소값은 1 이상이어야 합니다") Long scheduleId,
            HttpSession session) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commonService.createComment(dto, SessionHelper.getUserId(session), scheduleId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable @Min(value = 1, message = "일정 ID 최소값은 1 이상이어야 합니다") Long scheduleId,
            @PathVariable @Min(value = 1, message = "댓글 ID 최소값은 1 이상이어야 합니다") Long commentId,
            HttpSession session) {
        commonService.deleteComment(SessionHelper.getUserId(session), scheduleId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> modifyComment(
            @RequestBody CommentSaveRequestDto dto,
            @PathVariable @Min(value = 1, message = "일정 ID 최소값은 1 이상이어야 합니다") Long scheduleId,
            @PathVariable @Min(value = 1, message = "댓글 ID 최소값은 1 이상이어야 합니다") Long commentId,
            HttpSession session) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commonService.modifyComment(dto, SessionHelper.getUserId(session), scheduleId, commentId));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> findScheduleComment(
            @PathVariable @Min(value = 1, message = "일정 ID 최소값은 1 이상이어야 합니다") Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(readService.findScheduleComment(scheduleId));
    }
}
