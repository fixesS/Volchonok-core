package com.eduard.volchonokcore.database.entities;

import com.eduard.volchonokcore.database.entities.id.UserCourseId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_courses", schema = "public")
public class UserCourse {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(name = "user_id")
    private Integer userid;
    @Column(name = "course_id")
    private Integer courseid;



}
