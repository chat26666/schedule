package com.example.schedule.repository;
import com.example.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends BaseRepository<Schedule,Long> {

    @Query(value = "SELECT DISTINCT s FROM Schedule s " +
            "JOIN FETCH s.schedule_user u " +
            "LEFT JOIN FETCH s.Comments c " +
            "WHERE u.userId = :userId " +
            "ORDER BY s.updatedAt DESC",
             countQuery = "SELECT COUNT(DISTINCT s) FROM Schedule s JOIN s.schedule_user u WHERE u.userId = :userId")
    Page<Schedule> scheduleFindByUserId(@Param("userId") Long userId, Pageable page);

    @Query("SELECT DISTINCT s FROM Schedule s " +
            "JOIN s.schedule_user u " +
            "LEFT JOIN FETCH s.Comments c " +
            "WHERE u.userId = :userId " +
            "AND s.scheduleId = :scheduleId")
    Optional<Schedule> scheduleFindByScheduleId(@Param("userId")Long userId, @Param("scheduleId") Long scheduleId);
}

//찾아보니 JPA 는 조인시 SELECT 되는 컬럼이 아니면 지연 로딩이라고 연관관계인 엔티티를 get 으로 조회할 시점에 매번 추가로 Query 를 날린다고 하네요
//때문에 모든 데이터는 DTO 로 반환될 예정이니 FETCH 로 한번에 긁어오는 것이 더 성능상 이점이라고 판단해서 사용하였습니다