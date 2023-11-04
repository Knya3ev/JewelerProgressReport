package com.example.JewelerProgressReport.jewelry.jewelry_resize;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JewelryResizeRepository extends JpaRepository<JewelryResize, Long> {


    @Query("""
            SELECT jr
            FROM JewelryResize jr
            WHERE jr.jewelry.article = :article AND jr.resize.ringResizing = :resizesValue
            """)
    Optional<JewelryResize> getJewelryArticleAndResizes(@Param("article") String article,
                                                        @Param("resizesValue") String resizesValue);
}
