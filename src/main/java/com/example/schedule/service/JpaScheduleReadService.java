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

@Service
@RequiredArgsConstructor
public class JpaScheduleReadService implements ScheduleReadService {

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
    @Transactional
    @Override
    public List<ScheduleResponseDto> findScheduleAll(Long userId,Integer page,Integer size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("updatedAt").descending());
        Page<Schedule> schedulePage = scheduleRepo.scheduleFindByUserId(userId, pageable);
        List<Schedule> scheduleList = schedulePage.getContent();
        List<ScheduleResponseDto> dtoList = new ArrayList<>();
        for(Schedule schedule : scheduleList) {
            ScheduleResponseDto scheduleDto = modelMapper.map(schedule,ScheduleResponseDto.class)
                                                         .setName(schedule.getSchedule_user().getName())
                                                         .setComment(new ArrayList<>());
            List<Comment> commentList = schedule.getComments();
            if(commentList != null) {
                for (Comment comment : commentList) {
                    CommentResponseDto commentDto = modelMapper.map(comment, CommentResponseDto.class)
                                                               .setName(comment.getComment_user().getName())
                                                               .setUserId(comment.getComment_user().getUserId());
                    scheduleDto.getComment().add(commentDto);
                }
            }
            dtoList.add(scheduleDto);
        }
        return dtoList;
    }
    @Transactional
    @Override
    public ScheduleResponseDto findScheduleOne(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepo.scheduleFindByScheduleId(userId, scheduleId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"해당 일정이 존재하지 않습니다. 사용자 ID 및 일정 ID를 정확하게 입력해주십시오."));
        ScheduleResponseDto scheduleDto = modelMapper.map(schedule,ScheduleResponseDto.class)
                                                     .setName(schedule.getSchedule_user().getName())
                                                     .setComment(new ArrayList<>());
        List<Comment> commentList = schedule.getComments();
        if(commentList != null) {
            for (Comment comment : commentList) {
                CommentResponseDto commentDto = modelMapper.map(comment, CommentResponseDto.class)
                        .setName(comment.getComment_user().getName())
                        .setUserId(comment.getComment_user().getUserId());
                scheduleDto.getComment().add(commentDto);
            }
        }
        return scheduleDto;
    }
    @Transactional
    @Override
    public UserCommentInfoResponseDto findUserComment(Long userId) {
        User user = userRepo.CommentFindByUser(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"해당 유저가 존재하지 않습니다. 사용자 ID 정확하게 입력해주십시오."));
        UserCommentInfoResponseDto dto = modelMapper.map(user,UserCommentInfoResponseDto.class);
        dto.setComment(new ArrayList<>());  // 나중에 필드에서 new 수정

        for(Comment comment : user.getComments()) {
            CommentResponseDto commentDto = modelMapper.map(comment,CommentResponseDto.class);
            dto.getComment().add(commentDto);
        }
        return dto;
    }


}
