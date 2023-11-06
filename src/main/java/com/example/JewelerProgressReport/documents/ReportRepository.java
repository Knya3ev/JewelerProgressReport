package com.example.JewelerProgressReport.documents;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
            WHERE r.user.id = :personId
            """)
    List<Report> findByReportsByIdPerson(@Param("personId") Long id);

    @Query("""
            SELECT r FROM
            Report r
            WHERE r.createdDate >= :startDate AND r.createdDate <= :endDate AND r.user.id = :personId
            """)
    List<Report> getListForDocument(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("personId") Long personId);

    @Query(value = """
            SELECT *
            FROM report
            WHERE status = :status
            """,
            nativeQuery = true)
    List<Report> findAllByStatus(@Param("status") String status);

    @Query(value = """
            SELECT *
            FROM report
            WHERE status = :status AND article = :article
            """,
            nativeQuery = true)
    List<Report> findAllByStatusAndArticle(@Param("article") String article,
                                           @Param("status") String status);

    @Query(value = """
            SELECT count(r)
            FROM Report r
            WHERE r.shop_id = :shop AND r.status = :status
            """, nativeQuery = true)
    int countReportByStatus(@Param("shop") Long shopId, @Param("status") String status);

    @Query(value = """
            SELECT count(r)
            FROM Report r
            WHERE r.shop_id = :shop
            """, nativeQuery = true)
    int countAllResizes(@Param("shop") Long shopId);

    @Query(value = """
            SELECT *
            FROM report
            WHERE article = :article AND status = :status AND size_before = :before AND size_after = :after
            """, nativeQuery = true)
    Optional<Report> checkConsultation(@Param("article") String article,
                                       @Param("before") Double before,
                                       @Param("after") Double after,
                                       @Param("status") String status);

}
