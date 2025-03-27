package com.example.schedule.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "schedule")
public class schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long scheduleId;

    @Column(length = 40, nullable = false)
    String title;

    @Column(length = 200, nullable = false)
    String plan;

    @Temporal(TemporalType.TIMESTAMP)
    LocalDate createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    LocalDate updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schedule_comment")
    List<comment> comments;

    @ManyToOne
    @JoinColumn(name = "userId")
    user schedule_user;
}
