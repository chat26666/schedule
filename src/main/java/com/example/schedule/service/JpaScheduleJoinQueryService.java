package com.example.schedule.service;
import com.example.schedule.dto.CommentResponseDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Comment;
import com.example.schedule.entity.Schedule;
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

}
