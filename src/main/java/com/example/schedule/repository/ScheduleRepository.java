package com.example.schedule.repository;
import com.example.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    @Query(value = "SELECT DISTINCT s FROM Schedule s " +
            "JOIN FETCH s.schedule_user u " +
            "LEFT JOIN FETCH s.Comments c " +
            "WHERE u.userId = :userId " +
            "ORDER BY s.updatedAt DESC",
             countQuery = "SELECT COUNT(DISTINCT s) FROM Schedule s JOIN s.schedule_user u WHERE u.userId = :userId")
    Page<Schedule> scheduleFindByUserId(@Param("userId") Long userId, Pageable page);
    //lazy 로딩을 피하고자 fetch 로 한꺼번에 전부 데이터를 로딩했습니다

    @Query("SELECT DISTINCT s FROM Schedule s " +
            "JOIN s.schedule_user u " +
            "LEFT JOIN FETCH s.Comments c " +
            "WHERE u.userId = :userId " +
            "AND s.scheduleId = :scheduleId")
    Optional<Schedule> scheduleFindByScheduleId(@Param("userId")Long userId, @Param("scheduleId") Long scheduleId);


}
