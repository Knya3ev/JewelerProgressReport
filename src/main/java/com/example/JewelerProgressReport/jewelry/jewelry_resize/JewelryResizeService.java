package com.example.JewelerProgressReport.jewelry.jewelry_resize;


import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.jewelry.Jewelry;
import com.example.JewelerProgressReport.jewelry.JewelryRepository;
import com.example.JewelerProgressReport.jewelry.JewelryService;
import com.example.JewelerProgressReport.jewelry.resize.SizeRingRepository;
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



    public boolean CheckoutUniqueJewelry(ReportRequest report){
        Optional<Jewelry> jewelry = jewelryRepository.findByArticle(report.getArticle());

        if(jewelry.isEmpty()) {
            return false;
        }

        sizeRingService.checkoutSizeRingOrCreate(report.getSizeBefore(),report.getSizeAfter());

        Optional<JewelryResize> jewelryResize = jewelryResizeRepository.getJewelryArticleAndResizes(
                report.getArticle(),
                sizeRingService.getSizeAdjustmentStringFormatted(
                        report.getSizeBefore(),
                        report.getSizeAfter()));

        return jewelryResize.isPresent();
    }
}
