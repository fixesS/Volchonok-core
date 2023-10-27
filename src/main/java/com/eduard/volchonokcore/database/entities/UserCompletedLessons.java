package com.eduard.volchonokcore.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "userlessons", schema = "public")
public class UserCompletedLessons {
    @Id
    private Integer Id;
    @OneToOne
    @JoinColumn(name = "userid")
    private User user;
    @OneToMany
    @PrimaryKeyJoinColumn(name = "lessonid")
    private List<Lesson> lessons;
}
