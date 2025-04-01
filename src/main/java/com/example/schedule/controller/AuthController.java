package com.example.schedule.controller;
import com.example.schedule.dto.AuthLogin;
import com.example.schedule.dto.UserAuthRequestDto;
import com.example.schedule.service.ScheduleReadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final ScheduleReadService readService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> userLogin(
            @RequestBody @Validated({Default.class, AuthLogin.class}) UserAuthRequestDto dto,
            HttpServletRequest request) {

        if (request.getSession(false) != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "already logged in"));
        else {
            readService.validatePassword(dto);
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", dto.getUserId());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "login success"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> userLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "logout success"));

    }
}
