package com.eduard.volchonokcore.database.entities;

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
    @Column(name = "userid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column
    private String firstname;
    @Column
    private String surname;
    @Column
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
    private String phone;
    @Column
    private String email;
    @Column
    private String address;
    @Column(name = "class")
    private Integer classColumn;
    @Column
    private Integer coins;
    @ManyToMany(targetEntity = Course.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_courses",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "courseid"))
    private Set<Course> courses;
    @OneToMany(targetEntity = UserCompletedQuestion.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userid")
    private Set<UserCompletedQuestion> completedQuestions;
    @ManyToMany(targetEntity = Lesson.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_completed_lessons",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "lessonid"))
    private Set<Lesson> completedLessons;
    @ManyToMany(targetEntity = Test.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_completed_tests",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "testid"))
    private Set<Test> completedTests;
    @ManyToMany(targetEntity = Module.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_completed_modules",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "moduleid"))
    private Set<Module> completedModules;

    @ManyToMany(targetEntity = Course.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_completed_courses",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "courseid"))
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
