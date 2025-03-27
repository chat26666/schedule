package com.example.schedule.controller;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.service.CommonEntityService;
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
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final CommonEntityService commonService;

    @PostMapping
    public ResponseEntity<Map<String,Object>> createUser(@RequestBody @Validated UserSaveRequestDto dto) {
        return new ResponseEntity(commonService.createUser(dto), HttpStatus.CREATED);
    }
}
