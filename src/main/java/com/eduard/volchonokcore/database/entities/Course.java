package com.eduard.volchonokcore.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses", schema = "public")
public class Course {
    @Id
    @Column(name = "courseid")
    private Integer courseId;
    @Column
    private String name;
    @Column
    private String description;
}
