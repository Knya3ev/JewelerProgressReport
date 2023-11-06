package com.example.JewelerProgressReport.jewelry;


import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.documents.ReportMapper;
import com.example.JewelerProgressReport.documents.ReportRepository;
import com.example.JewelerProgressReport.documents.ReportService;
import com.example.JewelerProgressReport.documents.enums.StatusReport;
import com.example.JewelerProgressReport.documents.response.ResponseCounseling;
import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.jewelry.jewelry_resize.JewelryResize;
import com.example.JewelerProgressReport.jewelry.jewelry_resize.JewelryResizeRepository;
import com.example.JewelerProgressReport.jewelry.resize.Resize;
import com.example.JewelerProgressReport.jewelry.resize.SizeRingService;
import com.example.JewelerProgressReport.jewelry.response.JewelryResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JewelryService {
    private final JewelryRepository jewelryRepository;
    private final SizeRingService sizeRingService;
    private final JewelryResizeRepository jewelryResizeRepository;
    private final ReportMapper reportMapper;
    private final ReportRepository reportRepository;

    private Jewelry read(String article) {
        return jewelryRepository.findByArticle(article)
                .orElseThrow(() -> new HttpException("Jewelry with the article %s is not found".formatted(article), HttpStatus.NOT_FOUND));
    }

    public JewelryResponse get(String article) {
        return toJewelryResponse(read(article));
    }
    @Transactional
    public void createJewelryIfIsNotNullArticle(Report report) {

        if (report.getSizeBefore() == null || report.getSizeAfter() == null) {
            throw new HttpException("fields size before and size after cannot by empty", HttpStatus.BAD_REQUEST);
        }

        Optional<Jewelry> request = jewelryRepository.findByArticle(report.getArticle());

        Jewelry jewelry = request.orElseGet(() -> create(new Jewelry(report.getArticle(), report.getJewelleryProduct())));

        if(!report.getClient().getJewelries().contains(jewelry)){
            report.getClient().addJewelry(jewelry);
        }

        this.addSizeRing(report, jewelry);
    }

    @Transactional
    private Jewelry create(Jewelry jewelry) {
       return jewelryRepository.save(jewelry);
    }

    @Transactional
    private void addSizeRing(Report report, Jewelry jewelry) {
        Resize resize = sizeRingService
                .checkoutSizeRingOrCreate(report.getSizeBefore(), report.getSizeAfter());

        jewelryResizeRepository.save(
                JewelryResize.builder()
                        .jewelry(jewelry)
                        .resize(resize)
                        .build());
    }

    private List<ResponseCounseling> getConsultation(String article){
        return reportMapper.toResponseCounseling(
                reportRepository.findAllByStatusAndArticle(article, StatusReport.CONSULTATION.getCode()));
    }

    private JewelryResponse toJewelryResponse(Jewelry jewelry) {
        if (jewelry == null) {
            return null;
        }
        return JewelryResponse.builder()
                .id(jewelry.getId())
                .jewelleryProduct(jewelry.getJewelleryProduct().getRu())
                .article(jewelry.getArticle())
                .consultations(getConsultation(jewelry.getArticle()))
                .resizes(jewelry.getJewelryResizes().stream().map(i -> i.getResize().getRingResizing()).toList())
                .build();
    }
}
