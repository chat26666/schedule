package com.example.schedule.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "User")
@Getter
@Setter
public class User extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long userId;

    @Column(length = 40, nullable = false)
    String name;

    @Column(length = 70, nullable = false, unique = true)
    String email;

    @Column(length = 100, nullable = false)
    String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment_user")
    List<Comment> Comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schedule_user")
    List<Schedule> Schedules;
}
