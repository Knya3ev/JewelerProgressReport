package com.example.JewelerProgressReport.security.token;

import com.example.JewelerProgressReport.users.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
public class Token {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "token_seq", sequenceName = "token_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "jwt")
    private String jwt;

    @Column(name = "token_refresh")
    private String tokenRefresh;

    @Column(name = "expiration")
    private Date expiration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
