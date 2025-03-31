package com.example.schedule.repository;
import com.example.schedule.dto.UserInfoResponseDto;
import com.example.schedule.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User,Long> {

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN FETCH u.Comments c " +
            "WHERE u.userId = :userId " +
            "ORDER BY c.createdAt DESC")
    Optional<User> CommentFindByUser(@Param("userId") Long userId);

    @Query("SELECT new com.example.schedule.dto.UserInfoResponseDto(" +
            "u.userId, u.name, u.email, u.createdAt, u.updatedAt) " +
            "FROM User u " +
            "ORDER BY u.createdAt")
    List<UserInfoResponseDto> findAllUser();
}