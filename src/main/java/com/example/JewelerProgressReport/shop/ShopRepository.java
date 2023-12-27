package com.example.JewelerProgressReport.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {


    Optional<Shop> findByName(String name);

    @Query(value = """
            SELECT *
            FROM shop
            WHERE is_moderation = FALSE
            """, nativeQuery = true)
    List<Shop> findAllVerified();

    @Query(value = """
            SELECT *
            FROM shop
            WHERE is_moderation = TRUE
            """, nativeQuery = true)
    List<Shop> findAllModeration();

    @Query(value = """
            SELECT count(s)
            FROM Shop s
            WHERE is_moderation = TRUE
            """, nativeQuery = true)
    int countAllModeration();

    @Modifying
    @Query("""
            UPDATE Shop s
            SET s.isHaveJeweler = TRUE, s.numberOfJewelerMasters = 1
            WHERE s.id = :shopId
            """)
    void accessAddJeweler(@Param("shopId") Long shopId);

    @Modifying
    @Query("""
            UPDATE Shop s
            SET s.numberOfAdministrators = :count
            WHERE s.id = :shopId
            """)
    void editCountAdmin(@Param("shopId") Long shopId,
                        @Param("count") int count);

    @Modifying
    @Query("""
            UPDATE Shop s
            SET s.numberOfShopAssistants = :count
            WHERE s.id = :shopId
            """)
    void editCountShopAssistants(@Param("shopId") Long shopId,
                                 @Param("count") int count);

    @Modifying
    @Query("""
            UPDATE Shop s
            SET s.numberOfJewelerMasters = :count
            WHERE s.id = :shopId
            """)
    void editCountJeweler(@Param("shopId") Long shopId,
                          @Param("count") int count);

}
