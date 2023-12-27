package com.example.JewelerProgressReport.users.client;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    Optional<Client> findByNumberPhone(String numberPhone);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Client c SET
            c.lastVisit = CURRENT_TIMESTAMP,
            c.countVisits = c.countVisits + 1
            WHERE c.id = :clientId
            """)
    void updateLastVisitAndCountVisit(@Param("clientId") Long clientId);
}
