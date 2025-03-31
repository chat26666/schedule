package com.example.schedule.service;
import com.example.schedule.UserSaveRequestDto;
import com.example.schedule.config.PasswordEncoder;
import com.example.schedule.dto.*;
import com.example.schedule.entity.Comment;
import com.example.schedule.entity.Schedule;
import com.example.schedule.entity.User;
import com.example.schedule.entity.ValidationIdChecker;
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

    private <T extends ValidationIdChecker> void validateOwnership(Long id, T instance) {
        String errorMessage = instance.validateOwnership(id);
        if (errorMessage != null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
    }

    //해당 자원의 작성자 Id가  현재 접속한 유저의 userId 와 동일한지 혹은 해당 댓글이 이 게시글에 속한지를 점검합니다

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
        userRepo.deleteById(dto.getUserId());
    }

    @Transactional
    @Override
    public void modifyUser(UserSaveRequestDto dto, Long userId) {
        userRepo.findById(userId).get()
                .setName(dto.getName())
                .setEmail(dto.getEmail());
    }

    @Transactional
    @Override
    public Long createSchedule(ScheduleSaveRequestDto dto, Long userId) {
        Schedule schedule = modelMapper.map(dto, Schedule.class);
        User user = userRepo.findById(userId).get();
        user.addSchedule(schedule);
        return scheduleRepo.save(schedule).getScheduleId();
    }

    //연관관계 설정을 위해서 User 객체를 find 합니다

    @Transactional
    @Override
    public void modifySchedule(ScheduleSaveRequestDto dto, Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        validateOwnership(userId,schedule.getSchedule_user());
        schedule.setTitle(dto.getTitle()).setPlan(dto.getPlan());
    }

    @Transactional
    @Override
    public void deleteSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        validateOwnership(userId,schedule.getSchedule_user());
        User user = userRepo.findById(userId).get();
        user.removeSchedule(schedule);
        scheduleRepo.deleteById(scheduleId);
    }

    @Transactional
    @Override
    public Long createComment(CommentSaveRequestDto dto, Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        User user = userRepo.findById(userId).get();
        Comment comment = modelMapper.map(dto, Comment.class)
                .setComment_schedule(schedule)
                .setComment_user(user);
        user.addComment(comment);
        schedule.addComment(comment);
        return commentRepo.save(comment).getCommentId();
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long scheduleId, Long commentId) {
        Comment comment = commentRepo.findOrThrow(commentId, Comment.class.getSimpleName());
        Schedule schedule = scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        validateOwnership(scheduleId,comment.getComment_schedule());
        validateOwnership(userId,comment.getComment_user());
        User user = userRepo.findById(userId).get();
        schedule.removeComment(comment);
        user.removeComment(comment);
        commentRepo.deleteById(commentId);
    }

    @Transactional
    @Override
    public void modifyComment(CommentSaveRequestDto dto, Long userId, Long scheduleId, Long commentId) {
        Comment comment = commentRepo.findOrThrow(commentId, Comment.class.getSimpleName());
        scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        validateOwnership(scheduleId,comment.getComment_schedule());
        validateOwnership(userId,comment.getComment_user());
        comment.setMention(dto.getMention());
    }
}