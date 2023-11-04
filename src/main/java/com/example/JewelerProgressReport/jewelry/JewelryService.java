package com.example.JewelerProgressReport.jewelry;


import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.jewelry.jewelry_resize.JewelryResize;
import com.example.JewelerProgressReport.jewelry.jewelry_resize.JewelryResizeRepository;
import com.example.JewelerProgressReport.jewelry.resize.Resize;
import com.example.JewelerProgressReport.jewelry.resize.SizeRingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JewelryService {
    private final JewelryRepository jewelryRepository;
    private final SizeRingService sizeRingService;
    private final JewelryResizeRepository jewelryResizeRepository;

    public Jewelry read(String article) {
        return jewelryRepository.findByArticle(article)
                .orElseThrow(() -> new HttpException("Jewelry with the article %s is not found".formatted(article), HttpStatus.NOT_FOUND));
    }

    public void createJewelryIfIsNotNullArticle(Report report) {

        if (report.getSizeBefore() == null || report.getSizeAfter() == null) {
            throw new HttpException("fields size before and size after cannot by empty", HttpStatus.BAD_REQUEST);
        }

        try {

            read(report.getArticle());

        }catch (HttpException e){
            Jewelry jewelry = new Jewelry(report.getArticle(), report.getJewelleryProduct());
            this.create(jewelry);

            this.addSizeRing(report, jewelry);

            report.getClient().addJewelry(jewelry);
        }
    }

    @Transactional
    private void create(Jewelry jewelry) {
        jewelryRepository.save(jewelry);
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
}
