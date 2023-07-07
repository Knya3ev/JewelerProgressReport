package com.example.JewelerProgressReport.repository;

import com.example.JewelerProgressReport.entity.Client;
import com.example.JewelerProgressReport.entity.Jewelry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JewelryRepository extends JpaRepository<Jewelry,Long> {


    Optional<Jewelry> findByArticle(String article);
}
