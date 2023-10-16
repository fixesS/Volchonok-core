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
@IdClass(UserCourseId.class)
public class UserCourse {
    @Id
    @OneToOne
    @JoinColumn(name = "userid")
    private User user;
    @Id
    private Integer courseid;
    @OneToMany
    @JoinColumn(name = "courseid")
    private List<Course> courses;


}
