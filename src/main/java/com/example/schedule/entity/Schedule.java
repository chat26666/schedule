package com.example.schedule.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Schedule")
@Getter
@Setter
@Accessors(chain = true)
public class Schedule extends BaseEntity implements ValidationIdChecker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @Column(length = 40, nullable = false)
    private String title;

    @Column(length = 200, nullable = false)
    private String plan;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment_schedule")
    private List<Comment> Comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User schedule_user;

    public void addComment(Comment comment) {
        Comments.add(comment);
        comment.setComment_schedule(this);
    }

    public void removeComment(Comment comment) {
        Comments.remove(comment);
        comment.setComment_schedule(null);
    }

    public String validateOwnership(Long id) {
        return this.scheduleId.equals(id)
                ? null
                : "userId : 해당 일정의 댓글이 아닙니다";
    }
}
