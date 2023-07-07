package com.example.JewelerProgressReport.repository;


import com.example.JewelerProgressReport.entity.Report;
import com.example.JewelerProgressReport.model.response.ReportResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("""
            SELECT r
            FROM Report r
            WHERE r.client.id = :clientId
            """)
    List<Report> findByReportsByIdClient(@Param("clientId") Long id);

    @Query("""
            SELECT r
            FROM Report r
            WHERE r.person.id = :personId
            """)
    List<Report> findByReportsByIdPerson(@Param("personId") Long id);
}
