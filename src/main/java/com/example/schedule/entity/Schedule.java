package com.example.schedule.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.List;

@Entity
@Table(name = "Schedule")
@Getter
@Setter
@Accessors(chain = true)
public class Schedule extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @Column(length = 40, nullable = false)
    private String title;

    @Column(length = 200, nullable = false)
    private String plan;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schedule_comment")
    private List<Comment> Comments;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User schedule_user;
}
