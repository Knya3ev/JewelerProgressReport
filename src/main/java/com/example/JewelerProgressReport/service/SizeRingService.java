package com.example.JewelerProgressReport.service;

import com.example.JewelerProgressReport.entity.SizeRing;
import com.example.JewelerProgressReport.repository.SizeRingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SizeRingService {
    private final SizeRingRepository sizeRingRepository;
    @Transactional
    public void create(SizeRing sizeRing){
        sizeRingRepository.save(sizeRing);
    }


    public String getSizeAdjustmentStringFormatted(Double sizeBefore, Double sizeAfter){
        return String.format("%.2f -> %.2f", sizeBefore, sizeAfter);
    }

    public SizeRing checkoutSizeRingOrCreate(Double before, Double after){
        String size = getSizeAdjustmentStringFormatted(before,after);
        Optional<SizeRing> sizeRing1 = sizeRingRepository.findByRingResizing(size);

        if (sizeRing1.isPresent()) return sizeRing1.get();

        SizeRing sizeRing =
                SizeRing.builder()
                        .ringResizing(size)
                        .before(before)
                        .after(after)
                        .build();
        this.create(sizeRing);
        return sizeRing;
    }

}
