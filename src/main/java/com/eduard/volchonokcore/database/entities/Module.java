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
@Table(name = "modules", schema = "public")
public class Module {
    @Id
    @Column(name = "moduleid")
    private Integer moduleId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "courseid")
    private Course course;
    @Column
    private Integer number;

    @Column
    private String name;
    @Column
    private String description;

}
