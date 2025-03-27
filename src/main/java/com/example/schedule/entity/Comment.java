package com.example.schedule.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Comment")
@Getter
@Setter
public class Comment extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long commentId;

    @Column(length = 55, nullable = false)
    String mention;

    @ManyToOne
    @JoinColumn(name = "userId")
    User comment_user;

    @ManyToOne
    @JoinColumn(name = "scheduleId")
    Schedule schedule_comment;

}
