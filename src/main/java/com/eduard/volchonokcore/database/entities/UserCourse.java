package com.eduard.volchonokcore.database.entities;

import com.eduard.volchonokcore.database.entities.id.UserCourseId;
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
@Table(name = "usercourses", schema = "public")
public class UserCourse {
    @Id
    private Integer Id;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "userid")
    private User user;

    @OneToMany
    @PrimaryKeyJoinColumn(name = "courseid")
    private List<Course> courses;



}
