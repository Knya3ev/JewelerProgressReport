package com.example.JewelerProgressReport.jewelry;


import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.documents.ReportMapper;
import com.example.JewelerProgressReport.documents.ReportRepository;
import com.example.JewelerProgressReport.documents.enums.StatusReport;
import com.example.JewelerProgressReport.documents.response.ResponseCounseling;
import com.example.JewelerProgressReport.exception.HttpException;
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
    }

    @Transactional
    private Jewelry create(Jewelry jewelry) {
       return jewelryRepository.save(jewelry);
    }

    private List<ResponseCounseling> getConsultation(String article){
        return reportMapper.toResponseCounseling(
                reportRepository.findAllByStatusAndArticle(article, StatusReport.CONSULTATION.getCode()));
    }

    public boolean CheckoutUniqueJewelry(String article, Double before, Double after) {
        Optional<Jewelry> jewelry = jewelryRepository.findByArticle(article);

        if (jewelry.isEmpty()) {
            return false;
        }

        Optional<Report> jewelryResult = reportRepository.getJewelryArticleAndResizes(article,before,after);

        //TODO: проверка чтобы размер был либо больше самого маленького размера либо больше самого большого

        return jewelryResult.isPresent();
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
                .resizes(reportRepository.findAllByStatusAndArticle(jewelry.getArticle(), StatusReport.UNIQUE.getCode())
                        .stream()
                        .map(i -> reportMapper.sizeFormatted(i.getSizeBefore(),i.getSizeAfter())).toList())
                .build();
    }
}
