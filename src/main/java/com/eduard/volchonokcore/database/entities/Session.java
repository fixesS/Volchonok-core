package com.eduard.volchonokcore.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions_mobile", schema = "public")
public class Session {
    @Id
    @Column(columnDefinition = "BINARY(16)", name = "session_uuid")
    private UUID sessionUuid;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "useragent")
    private String userAgent;
    @Column
    private InetAddress ip;
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "lastrefresh")
    private LocalDateTime lastRefresh;
    @Column(name = "expiresin")
    private LocalDateTime expiresIn;


}
