package com.example.schedule.controller;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.dto.UserAuthRequestDto;
import com.example.schedule.service.CommonEntityService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final CommonEntityService commonService;

    @PostMapping
    public ResponseEntity<Map<String,Object>> createUser(@RequestBody @Validated UserSaveRequestDto dto) {
        return new ResponseEntity(commonService.createUser(dto), HttpStatus.CREATED);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@RequestBody @Validated UserAuthRequestDto dto, @PathVariable Long userId , HttpSession session) {
        System.out.println(session.getAttribute("userId") + "  " + userId );
        if(!session.getAttribute("userId").equals(userId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //나중에 예외 처리 수정 필요
        //삭제후 세션파기 필요
        commonService.deleteUser(dto, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
