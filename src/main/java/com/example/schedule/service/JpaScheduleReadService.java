package com.example.schedule.service;
import com.example.schedule.config.PasswordEncoder;
import com.example.schedule.dto.*;
import com.example.schedule.entity.Comment;
import com.example.schedule.entity.Schedule;
import com.example.schedule.entity.User;
import com.example.schedule.repository.CommentRepository;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.repository.UserRepository;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JpaScheduleReadService implements ScheduleReadService {

    private final CommentRepository commentRepo;
    private final ScheduleRepository scheduleRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

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
    public void authUser(UserAuthRequestDto dto) {
        User user = userRepo.findOrThrow(dto.getUserId(), User.class.getSimpleName());
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "password : 비밀번호가 틀렸습니다");
    }

    @Transactional
    @Override
    public List<UserInfoResponseDto> findAllUser() {
        return userRepo.findAllUser();
    }

    @Transactional
    @Override
    public UserInfoResponseDto findUser(Long userId) {
        User user = userRepo.findOrThrow(userId, User.class.getSimpleName());
        return modelMapper.map(user, UserInfoResponseDto.class);
    }

    @Transactional
    @Override
    public List<CommentResponseDto> findScheduleComment(Long scheduleId) {
        scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        return commentRepo.findByScheduleComment(scheduleId);
    }

    @Transactional
    @Override
    public CommentResponseDto findComment(Long scheduleId, Long commentId) {
        return commentRepo.findComment(scheduleId, commentId);
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
    public ScheduleResponseDto findScheduleOne(Long userId, Long scheduleId, boolean isMySchedule) {
        final String message;
        if (isMySchedule) message = "scheduleId : 해당 schedule 이(가) 존재하지 않습니다";
        else message = "message : userId 및 scheduleId 가 올바른지 확인해주십시오";
        Schedule schedule = scheduleRepo.scheduleFindByScheduleId(userId, scheduleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, message));
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
