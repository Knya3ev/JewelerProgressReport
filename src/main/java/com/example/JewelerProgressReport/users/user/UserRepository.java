package com.example.JewelerProgressReport.users.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername (String username);

    Optional<User> findByTelegramId(long telegramId);

    @Modifying
    @Query("""
            UPDATE User u
            SET u.countShops = :count
            WHERE u.id = :userId
            """)
    void editCountShopDirector(Long userId, int count);
}
