package com.example.schedule.service;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.config.PasswordEncoder;
import com.example.schedule.dto.UserAuthRequestDto;
import com.example.schedule.entity.User;
import com.example.schedule.repository.CommentRepository;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class JpaCommonEntityService implements CommonEntityService {

    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepo;
    private final ScheduleRepository scheduleRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Map<String, Long> createUser(UserSaveRequestDto dto) {
        User user = modelMapper.map(dto, User.class)
                               .setPassword(passwordEncoder.encode(dto.getPassword()));
        Long userId = userRepo.save(user).getUserId();
        return Map.of("userId", userId);
    }
    public void authUser(UserAuthRequestDto dto) {
        User user = userRepo.findById(dto.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "계정이 틀렸습니다"));
        if(!passwordEncoder.matches(dto.getPassword(),user.getPassword())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다.");
    }
    public void deleteUser(UserAuthRequestDto dto, Long userId) {
        authUser(dto);
        userRepo.deleteById(dto.getUserId());
    }

}
