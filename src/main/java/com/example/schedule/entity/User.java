package com.example.schedule.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User")
@Getter
@Setter
@Accessors(chain = true)
public class User extends BaseEntity implements ValidationIdChecker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 40, nullable = false)
    private String name;

    @Column(length = 70, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment_user")
    private List<Comment> Comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schedule_user")
    private List<Schedule> Schedules = new ArrayList<>();

    public void addSchedule(Schedule schedule) {
        Schedules.add(schedule);
        schedule.setSchedule_user(this);
    }

    public void removeSchedule(Schedule schedule) {
        Schedules.remove(schedule);
        schedule.setSchedule_user(null);
    }

    public void addComment(Comment comment) {
        Comments.add(comment);
        comment.setComment_user(this);
    }

    public void removeComment(Comment comment) {
        Comments.remove(comment);
        comment.setComment_user(null);
    }

    public String validateOwnership(Long id) {
        return this.userId.equals(id)
                ? null
                : "userId : 해당 자원의 작성자가 아닙니다";
    }
}
