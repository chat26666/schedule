package com.example.schedule.service;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.config.PasswordEncoder;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.ScheduleSaveRequestDto;
import com.example.schedule.entity.User;
import com.example.schedule.repository.CommentRepository;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

}
