package com.example.schedule.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "Schedule")
@Getter
@Setter
public class Schedule extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long scheduleId;

    @Column(length = 40, nullable = false)
    String title;

    @Column(length = 200, nullable = false)
    String plan;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schedule_comment")
    List<Comment> Comments;

    @ManyToOne
    @JoinColumn(name = "userId")
    User schedule_user;
}
