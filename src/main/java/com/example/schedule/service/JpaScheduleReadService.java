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

    //ResponseDto 로 변경하는 로직이 중복되서 따로 private 메서드로 분리하였습니다

    @Transactional(readOnly = true)
    @Override
    public void authUser(UserAuthRequestDto dto) {
        User user = userRepo.findOrThrow(dto.getUserId(), User.class.getSimpleName());
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "password : 비밀번호가 틀렸습니다");
    }

    //비밀번호 인증 로직입니다

    @Transactional(readOnly = true)
    @Override
    public List<UserInfoResponseDto> findAllUser() {
        return userRepo.findAllUser();
    }

    @Transactional(readOnly = true)
    @Override
    public UserInfoResponseDto findUser(Long userId) {
        User user = userRepo.findOrThrow(userId, User.class.getSimpleName());
        return modelMapper.map(user, UserInfoResponseDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentResponseDto> findScheduleComment(Long scheduleId) {
        scheduleRepo.findOrThrow(scheduleId, Schedule.class.getSimpleName());
        return commentRepo.findByScheduleComment(scheduleId);
    }

    //한 스케쥴의 댓글 전체를 조회합니다

    @Transactional(readOnly = true)
    @Override
    public CommentResponseDto findComment(Long scheduleId, Long commentId) {
        return commentRepo.findComment(scheduleId, commentId);
    }

    @Transactional(readOnly = true)
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

    //유저 Id 를 기반으로 해당 유저의 일정을 전체 검색합니다
    //페이지 및 사이즈 지정이 가능하고 미입력시 디폴트 값으로 검사하게 됩니다

    @Transactional(readOnly = true)
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

    //일정 1개를 검색하는 메서드이며 예외 발생시 두가지 경우의 수가 있어서 분기 처리하였습니다
    //로그인 상태로 자기자신의 userId 는 입력하지 않고 스케쥴 Id 로만 검색할 경우
    //혹은 다른 이의 일정을 검색할 경우 각기 다른 예외를 던집니다

    @Transactional(readOnly = true)
    @Override
    public UserCommentInfoResponseDto findUserComment(Long userId) {
        User user = userRepo.commentFindByUser(userId)
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

    //해당 유저의 전체 댓글 이력을 검색합니다
}
