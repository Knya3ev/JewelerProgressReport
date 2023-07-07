package com.example.JewelerProgressReport.repository;

import com.example.JewelerProgressReport.entity.Jewelry;
import com.example.JewelerProgressReport.entity.SizeRing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizeRingRepository extends JpaRepository<SizeRing,Long> {
    Optional<SizeRing> findByRingResizing(String size);
}
