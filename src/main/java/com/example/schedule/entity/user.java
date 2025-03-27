package com.example.schedule.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user")
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long userId;

    @Column(length = 40, nullable = false)
    String name;

    @Column(length = 70, nullable = false, unique = true)
    String email;

    @Column(length = 100, nullable = false)
    String password;

    @Temporal(TemporalType.TIMESTAMP)
    LocalDate createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    LocalDate updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schedule_user")
    List<comment> comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment_user")
    List<schedule> schedules;
}
