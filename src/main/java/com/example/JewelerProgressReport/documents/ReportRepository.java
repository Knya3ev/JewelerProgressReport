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
            WHERE r.createdDate >= :startDate AND r.createdDate <= :endDate AND r.user.id = :personId
            AND r.status IN ('UNQ', 'ORY', 'SRC')
            """)
    List<Report> getListForDocument(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    @Param("personId") Long personId);

    @Query("""
            SELECT r
            FROM Report r
            WHERE r.article = :article AND r.sizeBefore = :before AND r.sizeAfter = :after
            """)
    Optional<Report> getJewelryArticleAndResizes(@Param("article") String article,
                                                 @Param("before") Double before,
                                                 @Param("after") Double after);

    @Query(value = """
            SELECT *
            FROM report
            WHERE status = :status
            """, nativeQuery = true)
    List<Report> findAllByStatus(@Param("status") String status);

    @Query(value = """
            SELECT *
            FROM report
            WHERE status = :status AND article = :article
            """, nativeQuery = true)
    List<Report> findAllByStatusAndArticle(@Param("article") String article,
                                           @Param("status") String status);

    @Query("""
            SELECT
            new com.example.JewelerProgressReport.documents.SizeForJeweler(r.sizeBefore, r.sizeAfter)
            FROM Report r
            WHERE r.article = :article AND r.status IN ('UNQ','ORY')
            GROUP BY r.sizeBefore, r.sizeAfter
            ORDER BY  r.sizeBefore, r.sizeAfter
            """)
    List<SizeForJeweler> findAllByStatusOrdinaryAndUniqueAndArticle(@Param("article") String article);

    @Query(value = """
            SELECT *
            FROM report
            WHERE article = :article AND status = :status AND size_before = :before AND size_after = :after
            """, nativeQuery = true)
    Optional<Report> checkConsultation(@Param("article") String article,
                                       @Param("before") Double before,
                                       @Param("after") Double after,
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

    @Query("""
            SELECT COALESCE(
            (SELECT
            CASE WHEN (r.sizeAfter - r.sizeBefore) < :value
            THEN TRUE ELSE FALSE END
            FROM Report r
            WHERE r.article = :article AND r.sizeAfter > r.sizeBefore AND r.status IN ('UNQ','ORY')
            ORDER BY (r.sizeAfter - r.sizeBefore) DESC LIMIT 1), TRUE)
            """)
    boolean isMoreValueByUpperLimit(@Param("article") String article,
                                    @Param("value") Double value);

    @Query("""
            SELECT COALESCE(
            (SELECT
            CASE WHEN (r.sizeBefore - r.sizeAfter) < :value
            THEN TRUE ELSE FALSE END
            FROM Report r
            WHERE r.article = :article AND  r.sizeBefore > r.sizeAfter AND r.status IN ('UNQ','ORY')
            ORDER BY (r.sizeBefore - r.sizeAfter) DESC LIMIT 1), TRUE)
            """)
    boolean isMoreValueByLowerLimit(@Param("article") String article,
                                    @Param("value") Double value);

}
