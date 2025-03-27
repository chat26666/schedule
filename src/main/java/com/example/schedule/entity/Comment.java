package com.example.schedule.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "Comment")
@Getter
@Setter
@Accessors(chain = true)
public class Comment extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(length = 55, nullable = false)
    private String mention;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User comment_user;

    @ManyToOne
    @JoinColumn(name = "scheduleId")
    private Schedule schedule_comment;

}
