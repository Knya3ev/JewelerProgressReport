package com.example.JewelerProgressReport.jewelry;

import com.example.JewelerProgressReport.jewelry.Resize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizeRingRepository extends JpaRepository<Resize,Long> {
    Optional<Resize> findByRingResizing(String size);
}
