package com.eduard.volchonokcore.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column
    private String login;
    @Column
    private String password;
    @Column
    private String phone;
    @Column
    private String email;
    @Column
    private String address;
    @Column(name = "class")
    private Integer classColumn;
    @Column
    private String coins;

}
