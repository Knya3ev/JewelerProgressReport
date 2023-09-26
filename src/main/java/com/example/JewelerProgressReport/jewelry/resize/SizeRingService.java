package com.example.JewelerProgressReport.jewelry.resize;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SizeRingService {
    private final SizeRingRepository sizeRingRepository;
    @Transactional
    public void create(Resize resize){
        sizeRingRepository.save(resize);
    }


    public String getSizeAdjustmentStringFormatted(Double sizeBefore, Double sizeAfter){
        return String.format("%.2f -> %.2f", sizeBefore, sizeAfter);
    }

    public Resize checkoutSizeRingOrCreate(Double before, Double after){
        String size = getSizeAdjustmentStringFormatted(before,after);
        Optional<Resize> sizeRing1 = sizeRingRepository.findByRingResizing(size);

        if (sizeRing1.isPresent()) return sizeRing1.get();

        Resize resize =
                Resize.builder()
                        .ringResizing(size)
                        .before(before)
                        .after(after)
                        .build();
        this.create(resize);
        return resize;
    }

}
