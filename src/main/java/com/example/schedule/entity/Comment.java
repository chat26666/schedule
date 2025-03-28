package com.example.schedule.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User comment_user;

    @ManyToOne
    @JoinColumn(name = "scheduleId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Schedule comment_schedule;

}
