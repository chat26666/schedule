package com.example.schedule.service;
import com.example.schedule.UserSaveRequestDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

//해당 서비스는 각 Repository 의 C,U,D 를 담당합니다

@Service
@RequiredArgsConstructor
public class JpaCommonEntityService implements CommonEntityService {

    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepo;
    private final ScheduleRepository scheduleRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;
    private final ScheduleReadService joinService;

    private void checkId(Long session_id, Long id, String message) {
        if (!session_id.equals(id))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
    }

    @Transactional
    @Override
    public void authUser(UserAuthRequestDto dto) {
        User user = userRepo.findOrThrow(dto.getUserId(), User.class.getSimpleName());
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다.");
    }

    @Transactional
    @Override
    public Map<String, Long> createUser(UserSaveRequestDto dto) {
        User user = modelMapper.map(dto, User.class)
                .setPassword(passwordEncoder.encode(dto.getPassword()));
        Long userId = userRepo.save(user).getUserId();
        return Map.of("userId", userId);
    }

    @Transactional
    @Override
    public void deleteUser(UserAuthRequestDto dto, Long userId) {
        authUser(dto);
        userRepo.deleteById(dto.getUserId());
    }

    @Transactional
    @Override
    public UserInfoResponseDto modifyUser(UserSaveRequestDto dto, Long userId) {
        User user = userRepo.findById(userId).get()
                .setName(dto.getName())
                .setEmail(dto.getEmail());
        userRepo.flush();
        return modelMapper.map(user, UserInfoResponseDto.class);
        // 플러시를 안해주면 Dto 에 수정시간이 반영되지 않음
    }

    @Transactional
    @Override
    public UserInfoResponseDto findUser(Long userId) {
        User user = userRepo.findOrThrow(userId, User.class.getSimpleName());
        return modelMapper.map(user, UserInfoResponseDto.class);
    }

    @Transactional
    @Override
    public ScheduleResponseDto createSchedule(ScheduleSaveRequestDto dto, Long userId) {
        Schedule schedule = modelMapper.map(dto, Schedule.class);
        User user = userRepo.findById(userId).get();
        user.addSchedule(schedule);
        return modelMapper.map(scheduleRepo.save(schedule), ScheduleResponseDto.class)
                .setName(user.getName());
    }

    @Transactional
    @Override
    public ScheduleResponseDto modifySchedule(ScheduleSaveRequestDto dto, Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        Long check_userId = schedule.getSchedule_user().getUserId();
        checkId(userId, check_userId, "해당 게시글의 주인이 아닙니다");
        schedule.setTitle(dto.getTitle()).setPlan(dto.getPlan());
        scheduleRepo.flush();
        return joinService.findScheduleOne(userId, scheduleId);
    }

    @Transactional
    @Override
    public void deleteSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        Long check_userId = schedule.getSchedule_user().getUserId();
        checkId(userId, check_userId, "해당 게시글의 주인이 아닙니다");
        User user = userRepo.findById(userId).get();
        user.removeSchedule(schedule);
        scheduleRepo.deleteById(scheduleId);
    }

    @Transactional
    @Override
    public CommentResponseDto createComment(CommentSaveRequestDto dto, Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        User user = userRepo.findById(userId).get();
        Comment comment = modelMapper.map(dto, Comment.class)
                .setComment_schedule(schedule)
                .setComment_user(user);
        user.addComment(comment);
        schedule.addComment(comment);
        return modelMapper.map(commentRepo.save(comment), CommentResponseDto.class)
                .setUserId(user.getUserId())
                .setName(user.getName());
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long scheduleId, Long commentId) {
        Comment comment = commentRepo.findOrThrow(commentId, Comment.class.getSimpleName());
        Long check_userId = comment.getComment_user().getUserId();
        checkId(userId, check_userId, "해당 댓글의 주인이 아닙니다");
        Schedule schedule = scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        Long check_scheduleId = comment.getComment_schedule().getScheduleId();
        checkId(scheduleId, check_scheduleId, "해당 게시글의 댓글이 아닙니다");
        User user = userRepo.findById(userId).get();
        schedule.removeComment(comment);
        user.removeComment(comment);
        commentRepo.deleteById(commentId);
    }

    @Transactional
    @Override
    public CommentResponseDto modifyComment(CommentSaveRequestDto dto, Long userId, Long scheduleId, Long commentId) {
        Comment comment = commentRepo.findOrThrow(commentId, Comment.class.getSimpleName());
        Long check_userId = comment.getComment_user().getUserId();
        checkId(userId, check_userId, "해당 댓글의 주인이 아닙니다");
        scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        Long check_scheduleId = comment.getComment_schedule().getScheduleId();
        checkId(scheduleId, check_scheduleId, "해당 게시글의 댓글이 아닙니다");
        comment.setMention(dto.getMention());
        User user = userRepo.findById(userId).get();
        return modelMapper.map(comment, CommentResponseDto.class)
                .setName(user.getName())
                .setUserId(user.getUserId());
    }
}