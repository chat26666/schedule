package com.example.schedule.entity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "comment")
public class comment extends base {

    @Id
    @GeneratedValue(GenerationType.IDENTITY)
    long commentId;

    @Column(length = 55, nullable = false)
    String mention;

    @ManyToOne
    @JoinColumn(name = "userId")
    user comment_user;

    @ManyToOne
    @JoinColumn(name = "scheduleId")
    schedule schedule_comment;

}
