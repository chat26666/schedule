package com.example.schedule.repository;
import com.example.schedule.dto.CommentResponseDto;
import com.example.schedule.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long>  {

    @Query("SELECT new com.example.schedule.dto.CommentResponseDto(" +
            "c.commentId, u.userId, u.name, c.mention, c.createdAt, c.updatedAt) " +
            "FROM Comment c " +
            "JOIN c.comment_user u " +
            "JOIN c.comment_schedule s " +
            "WHERE s.scheduleId = :scheduleId " +
            "ORDER BY c.createdAt ASC")
    List<CommentResponseDto> findByScheduleComment(@Param("scheduleId") Long scheduleId);

}
