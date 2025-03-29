package com.example.schedule.repository;
import com.example.schedule.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User,Long> {

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN FETCH u.Comments c " +
            "WHERE u.userId = :userId " +
            "ORDER BY c.createdAt DESC")
    Optional<User> CommentFindByUser(@Param("userId") Long userId);
}

//유저가 쓴 댓글 이력을 전부 조회하는 기능입니다