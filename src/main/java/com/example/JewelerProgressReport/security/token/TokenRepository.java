package com.example.JewelerProgressReport.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
            SELECT t
            FROM Token t
            WHERE t.tokenRefresh = :token
            """)
    Optional<Token> findByRefreshToken(@Param("token") String token);

    @Query("""
            SELECT t
            FROM Token t
            WHERE t.user.id = :userId
            """)
    Optional<Token> findByUser(@Param("userId") Long userId);

}
