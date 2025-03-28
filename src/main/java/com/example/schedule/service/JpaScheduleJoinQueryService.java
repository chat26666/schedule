package com.example.schedule.service;
import com.example.schedule.dto.CommentResponseDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.CommentRepository;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class JpaScheduleJoinQueryService implements ScheduleJoinQueryService {

    private final CommentRepository commentRepo;
    private final ScheduleRepository scheduleRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public List<CommentResponseDto> findScheduleComment(Long scheduleId) {
        scheduleRepo.findById(scheduleId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 존재하지 않습니다."));
        return commentRepo.findByScheduleComment(scheduleId);
    }

}
