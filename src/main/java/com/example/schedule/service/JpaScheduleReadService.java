package com.example.schedule.service;
import com.example.schedule.dto.CommentResponseDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.UserCommentInfoResponseDto;
import com.example.schedule.entity.Comment;
import com.example.schedule.entity.Schedule;
import com.example.schedule.entity.User;
import com.example.schedule.repository.CommentRepository;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

// 서비스 클래스의 코드가 방대해지고, 여러 테이블 조인 및 뷰 구성을 위한 로직이 복잡해져 이를 별도의 클래스로 분리하여 관리합니다

@Service
@RequiredArgsConstructor
public class JpaScheduleReadService implements ScheduleReadService {

    private final CommentRepository commentRepo;
    private final ScheduleRepository scheduleRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;

    private ScheduleResponseDto convertToScheduleResponseDto(Schedule schedule) {
        ScheduleResponseDto scheduleDto = modelMapper.map(schedule, ScheduleResponseDto.class)
                .setName(schedule.getSchedule_user().getName());

        List<Comment> commentList = schedule.getComments();
        for (Comment comment : commentList) {
            CommentResponseDto commentDto = modelMapper.map(comment, CommentResponseDto.class)
                    .setName(comment.getComment_user().getName())
                    .setUserId(comment.getComment_user().getUserId());
            scheduleDto.getComment().add(commentDto);
        }
        return scheduleDto;
    }

    @Transactional
    @Override
    public List<CommentResponseDto> findScheduleComment(Long scheduleId) {
        scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        return commentRepo.findByScheduleComment(scheduleId);
    }

    @Transactional
    @Override
    public List<ScheduleResponseDto> findScheduleAll(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<Schedule> schedulePage = scheduleRepo.scheduleFindByUserId(userId, pageable);
        List<Schedule> scheduleList = schedulePage.getContent();

        List<ScheduleResponseDto> dtoList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            dtoList.add(convertToScheduleResponseDto(schedule));
        }
        return dtoList;
    }

    @Transactional
    @Override
    public ScheduleResponseDto findScheduleOne(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepo.scheduleFindByScheduleId(userId, scheduleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "scheduleId : 해당 schedule 이(가) 존재하지 않습니다 올바른 user 인지 혹은 scheduleId 를 확인해주십시오"));
        return convertToScheduleResponseDto(schedule);
    }

    @Transactional
    @Override
    public UserCommentInfoResponseDto findUserComment(Long userId) {
        User user = userRepo.CommentFindByUser(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "userId : 해당 user 이(가) 존재하지 않습니다"));
        UserCommentInfoResponseDto dto = modelMapper.map(user, UserCommentInfoResponseDto.class);

        for (Comment comment : user.getComments()) {
            CommentResponseDto commentDto = modelMapper.map(comment, CommentResponseDto.class);
            dto.getComment().add(commentDto);
        }
        return dto;
    }
}
