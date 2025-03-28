package com.example.schedule.controller;
import com.example.schedule.dto.CommentResponseDto;
import com.example.schedule.dto.CommentSaveRequestDto;
import com.example.schedule.service.CommonEntityService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules/{scheduleId}/comments")
@Validated
@RequiredArgsConstructor
public class CommentController {

    private final CommonEntityService commonService;
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody @Validated CommentSaveRequestDto dto, @PathVariable Long scheduleId, HttpSession session) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commonService.createComment(dto,(Long)session.getAttribute("userId"), scheduleId));
    }
}
