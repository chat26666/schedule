package com.example.schedule.repository;
import com.example.schedule.dto.CommentResponseDto;
import com.example.schedule.entity.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends BaseRepository<Comment,Long>  {

    @Query("SELECT new com.example.schedule.dto.CommentResponseDto(" +
            "c.commentId, u.userId, u.name, c.mention, c.createdAt, c.updatedAt) " +
            "FROM Comment c " +
            "JOIN c.comment_user u " +
            "JOIN c.comment_schedule s " +
            "WHERE s.scheduleId = :scheduleId " +
            "ORDER BY c.createdAt ASC")
    List<CommentResponseDto> findByScheduleComment(@Param("scheduleId") Long scheduleId);

    @Query("SELECT new com.example.schedule.dto.CommentResponseDto(" +
            "c.commentId, u.userId, u.name, c.mention, c.createdAt, c.updatedAt) " +
            "FROM Comment c " +
            "JOIN c.comment_user u " +
            "JOIN c.comment_schedule s " +
            "WHERE s.scheduleId = :scheduleId " +
            "AND c.commentId = :commentId")
    CommentResponseDto findComment(@Param("scheduleId")Long scheduleId, @Param("commentId")Long commentId);

}

//Join 해서 DTO 형태로 한번에 셀렉합니다
//댓글은 먼저 생성된 순서대로 조회되므로 오름차순 정렬입니다