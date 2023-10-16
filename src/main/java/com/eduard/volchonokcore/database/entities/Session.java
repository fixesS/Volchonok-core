package com.eduard.volchonokcore.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetAddress;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions", schema = "public")
public class Session {
    @Id
    private Integer sessionId;
    @OneToOne
    @JoinColumn(name = "userid")
    private User user;
    @Column
    private String userAgent;
    @Column
    private InetAddress ip;
    @Column
    private LocalDateTime created;
    @Column
    private LocalDateTime lastRefresh;
    @Column
    private LocalDateTime expiresIn;


}
