package com.example.JewelerProgressReport.jewelry.jewelry_resize;


import com.example.JewelerProgressReport.documents.request.ReportCounselingRequest;
import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.jewelry.Jewelry;
import com.example.JewelerProgressReport.jewelry.JewelryRepository;
import com.example.JewelerProgressReport.jewelry.resize.SizeRingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JewelryResizeService {

    private final JewelryResizeRepository jewelryResizeRepository;
    private final JewelryRepository jewelryRepository;
    private final SizeRingService sizeRingService;


    public boolean CheckoutUniqueJewelry(ReportRequest report) {
        return CheckoutUniqueJewelry(report.getArticle(), report.getSizeBefore(), report.getSizeAfter());
    }

    public boolean CheckoutUniqueJewelry(ReportCounselingRequest report) {
        return CheckoutUniqueJewelry(report.getArticle(), report.getSizeBefore(), report.getSizeAfter());
    }

    private boolean CheckoutUniqueJewelry(String article, Double before, Double after) {
        Optional<Jewelry> jewelry = jewelryRepository.findByArticle(article);

        if (jewelry.isEmpty()) {
            return false;
        }

        sizeRingService.checkoutSizeRingOrCreate(before, after);

        Optional<JewelryResize> jewelryResize = jewelryResizeRepository.getJewelryArticleAndResizes(
                article,
                sizeRingService.getSizeAdjustmentStringFormatted(before, after));

        return jewelryResize.isPresent();
    }
}
