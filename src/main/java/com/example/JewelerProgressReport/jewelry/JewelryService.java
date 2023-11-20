package com.example.JewelerProgressReport.jewelry;


import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.documents.ReportMapper;
import com.example.JewelerProgressReport.documents.ReportRepository;
import com.example.JewelerProgressReport.documents.enums.StatusReport;
import com.example.JewelerProgressReport.documents.request.ReportCounselingRequest;
import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.documents.response.ResponseCounseling;
import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.jewelry.enums.JewelleryProduct;
import com.example.JewelerProgressReport.jewelry.response.JewelryResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JewelryService {
    private final JewelryRepository jewelryRepository;
    private final ReportMapper reportMapper;
    private final ReportRepository reportRepository;

    private Jewelry read(String article) {
        return jewelryRepository.findByArticle(article)
                .orElseThrow(() -> new HttpException("Jewelry with the article %s is not found".formatted(article), HttpStatus.NOT_FOUND));
    }

    public JewelryResponse get(String article) {
        return toJewelryResponse(read(article));
    }

    public Jewelry getOrCreate(String article, JewelleryProduct jewelleryProduct) {
        Optional<Jewelry> jewelry = jewelryRepository.findByArticle(article);

        if(jewelry.isEmpty()){
            return create(new Jewelry(article, jewelleryProduct));
        }

        return jewelry.get();
    }

    @Transactional
    private Jewelry create(Jewelry jewelry) {
        return jewelryRepository.save(jewelry);
    }

    @Transactional
    public void addSizeForJewelry(Report report) {

        if (report.getSizeBefore() == null || report.getSizeAfter() == null) {
            throw new HttpException("fields size before and size after cannot by empty", HttpStatus.BAD_REQUEST);
        }

        if(isUniqueJewelry(report.getArticle(),report.getSizeBefore(),report.getSizeAfter())){

            Jewelry jewelry = read(report.getArticle());

            if (report.getSizeBefore() > report.getSizeAfter()) {
                jewelry.setLowerLimit(report.getSizeBefore() - report.getSizeAfter());
            }

            if (report.getSizeBefore() < report.getSizeAfter()) {
                jewelry.setUpperLimit(report.getSizeAfter() - report.getSizeBefore());
            }
        }
    }

    private boolean isUniqueJewelry(String article, Double before, Double after) {

        if(jewelryRepository.findByArticle(article).isEmpty()){
            return true;
        }

        if (before > after) {
            return reportRepository.isMoreValueByLowerLimit(article, (before - after));
        }

        if (before < after) {
            return reportRepository.isMoreValueByUpperLimit(article, (after - before));
        }

        return false;
    }

    public boolean isUniqueJewelry(ReportRequest request) {
        return isUniqueJewelry(request.getArticle(), request.getSizeBefore(), request.getSizeAfter());
    }

    public boolean isUniqueJewelry(ReportCounselingRequest request) {
        return isUniqueJewelry(request.getArticle(), request.getSizeBefore(), request.getSizeAfter());
    }

    private List<ResponseCounseling> getConsultation(String article) {
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
                .lowerLimit(jewelry.getLowerLimit())
                .upperLimit(jewelry.getUpperLimit())
                .consultations(getConsultation(jewelry.getArticle()))
                .resizes(
                        reportRepository.findAllByStatusOrdinaryAndUniqueAndArticle(jewelry.getArticle())
                        .stream()
                        .map(i -> reportMapper.sizeFormatted(i.getSizeBefore(), i.getSizeAfter())).toList()).build();
    }
}
