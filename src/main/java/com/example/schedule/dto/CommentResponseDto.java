package com.example.schedule.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private Long userId;
    private String name;
    private String mention;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
