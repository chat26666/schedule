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

@Service
@RequiredArgsConstructor
public class JpaCommonEntityService implements CommonEntityService {
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepo;
    private final ScheduleRepository scheduleRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;
    private final ScheduleJoinQueryService joinService;

    @Transactional
    @Override
    public Map<String, Long> createUser(UserSaveRequestDto dto) {
        User user = modelMapper.map(dto, User.class).setPassword(passwordEncoder.encode(dto.getPassword()));
        Long userId = userRepo.save(user).getUserId();
        return Map.of("userId", userId);
    }

    @Transactional
    @Override
    public void authUser(UserAuthRequestDto dto) {
        User user = userRepo.findById(dto.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "계정이 틀렸습니다"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다.");
    }

    @Transactional
    @Override
    public void deleteUser(UserAuthRequestDto dto, Long userId) {
        authUser(dto);
        userRepo.deleteById(dto.getUserId());
    }

    @Transactional
    @Override
    public UserResponseDto modifyUser(UserSaveRequestDto dto, Long userId) {
        User user = userRepo.findById(userId).get().setName(dto.getName()).setEmail(dto.getEmail());
        userRepo.flush();
        //플러시를 안해주면 dto 에 수정시간이 반영되지 않음
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Transactional
    @Override
    public UserResponseDto findUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "계정이 존재하지 않습니다."));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Transactional
    @Override
    public ScheduleResponseDto createSchedule(ScheduleSaveRequestDto dto, Long userId) {
        Schedule schedule = modelMapper.map(dto, Schedule.class);
        User user = userRepo.findById(userId).get();
        user.addSchedule(schedule);
        return modelMapper.map(scheduleRepo.save(schedule), ScheduleResponseDto.class).setName(user.getName());
    }

    @Transactional
    @Override
    public ScheduleResponseDto modifySchedule(ScheduleSaveRequestDto dto, Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepo.findById(scheduleId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다."));
        Long check_userId = schedule.getSchedule_user().getUserId();
        if (!userId.equals(check_userId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 게시글의 주인이 아닙니다.");
        //해당 인증로직 중복처리 필요
        schedule.setTitle(dto.getTitle()).setPlan(dto.getPlan());
        scheduleRepo.flush();
        return joinService.findScheduleOne(userId,scheduleId);
    }

    @Transactional
    @Override
    public void deleteSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepo.findById(scheduleId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다."));
        Long check_userId = schedule.getSchedule_user().getUserId();
        if (!userId.equals(check_userId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 게시글의 주인이 아닙니다.");

        //해당 인증로직 중복처리 필요

        User user = userRepo.findById(userId).get();
        user.removeSchedule(schedule);
        scheduleRepo.deleteById(scheduleId);
    }

    @Transactional
    @Override
    public CommentResponseDto createComment(CommentSaveRequestDto dto, Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepo.findById(scheduleId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 존재하지 않습니다."));

        //해당 인증로직 중복처리 필요

        User user = userRepo.findById(userId).get();
        Comment comment = modelMapper.map(dto, Comment.class).setComment_schedule(schedule).setComment_user(user);
        user.addComment(comment);
        schedule.addComment(comment);
        return modelMapper.map(commentRepo.save(comment), CommentResponseDto.class).setUserId(user.getUserId()).setName(user.getName());
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long scheduleId, Long commentId) {
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));
        Long check_userId = comment.getComment_user().getUserId();
        if (!userId.equals(check_userId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 댓글의 주인이 아닙니다.");
        Schedule schedule = scheduleRepo.findById(scheduleId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 존재하지 않습니다."));
        Long check_scheduleId = comment.getComment_schedule().getScheduleId();
        if (!scheduleId.equals(check_scheduleId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 게시글의 댓글이 아닙니다.");
        User user = userRepo.findById(userId).get();

        //해당 인증로직 중복처리 필요

        schedule.removeComment(comment);
        user.removeComment(comment);
        commentRepo.deleteById(commentId);
    }
    @Transactional
    @Override
    public CommentResponseDto modifyComment(CommentSaveRequestDto dto,Long userId, Long scheduleId, Long commentId) {
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));
        Long check_userId = comment.getComment_user().getUserId();
        if (!userId.equals(check_userId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 댓글의 주인이 아닙니다.");
        scheduleRepo.findById(scheduleId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 존재하지 않습니다."));

        //해당 인증로직 중복처리 필요

        Long check_scheduleId = comment.getComment_schedule().getScheduleId();
        if (!scheduleId.equals(check_scheduleId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 게시글의 댓글이 아닙니다.");
        comment.setMention(dto.getMention());
        User user = userRepo.findById(userId).get();

        return modelMapper.map(comment,CommentResponseDto.class).setName(user.getName()).setUserId(user.getUserId());
    }
}
