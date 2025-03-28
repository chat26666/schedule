package com.example.schedule.controller;
import com.example.schedule.dto.UserAuthRequestDto;
import com.example.schedule.service.CommonEntityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    private final CommonEntityService commonService;

      @PostMapping("/login")
      public ResponseEntity<Map<String, String>> userLogin(@RequestBody @Validated UserAuthRequestDto dto, HttpServletRequest request) {

          if(request.getSession(false) != null) {
              System.out.println(request.getSession(false).getAttribute("userId"));
              return new ResponseEntity<>(Map.of("message","already logged in"), HttpStatus.OK);}
          else {
              commonService.authUser(dto);
              HttpSession session = request.getSession(true);
              session.setAttribute("userId",dto.getUserId());
              return new ResponseEntity<>(Map.of("message","login success"),HttpStatus.OK);
          }
      }


}
