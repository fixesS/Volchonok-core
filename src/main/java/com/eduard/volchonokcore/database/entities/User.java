package com.eduard.volchonokcore.database.entities;

import com.eduard.volchonokcore.security.encryption.UserDataEncryptor;
import com.eduard.volchonokcore.security.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "public")
public class User implements UserDetails {//todo ФИО
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column
    @Convert(converter = UserDataEncryptor.class)
    private String firstname;
    @Column
    @Convert(converter = UserDataEncryptor.class)
    private String surname;
    @Column
    @Convert(converter = UserDataEncryptor.class)
    private String middlename;
    @Column
    private String login;
    @Column
    private String avatar;
    @Column
    private Integer level;
    @Column
    private String password;
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column
    @Convert(converter = UserDataEncryptor.class)
    private String phone;
    @Column
    @Convert(converter = UserDataEncryptor.class)
    private String email;
    @Column
    @Convert(converter = UserDataEncryptor.class)
    private String address;
    @Column(name = "class")
    private Integer classColumn;
    @Column
    private Integer coins;
    @ManyToMany(targetEntity = Course.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses;
    @ManyToMany(targetEntity = Lesson.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_completed_lessons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id"))
    private Set<Lesson> completedLessons;
    @ManyToMany(targetEntity = Test.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_completed_tests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "test_id"))
    private Set<Test> completedTests;
    @ManyToMany(targetEntity = Module.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_completed_modules",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id"))
    private Set<Module> completedModules;

    @ManyToMany(targetEntity = Course.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_completed_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> completedCourses;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
