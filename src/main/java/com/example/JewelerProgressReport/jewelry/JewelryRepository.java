package com.example.JewelerProgressReport.jewelry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JewelryRepository extends JpaRepository<Jewelry,Long> {
    Optional<Jewelry> findByArticle(String article);
}
