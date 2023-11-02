package com.example.JewelerProgressReport.jewelry;


import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.jewelry.enums.JewelleryProduct;
import com.example.JewelerProgressReport.jewelry.resize.Resize;
import com.example.JewelerProgressReport.jewelry.resize.SizeRingService;
import com.example.JewelerProgressReport.users.client.Client;
import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.users.client.ClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JewelryService {
    private final JewelryRepository jewelryRepository;
    private final SizeRingService sizeRingService;
    private final ClientService clientService;

    public Jewelry read(String article) {
        return jewelryRepository.findByArticle(article)
                .orElseThrow(() -> new HttpException("Jewelry with the article %s is not found".formatted(article), HttpStatus.NOT_FOUND));
    }

    public void createJewelryIfIsNotNullArticle(Report report) {

        if (report.getSizeBefore() == null || report.getSizeAfter() == null) {
            throw new HttpException("fields size before and size after cannot by empty", HttpStatus.BAD_REQUEST);
        }

        Jewelry jewelry = this.checkoutJewelryOrCreate(
                report.getArticle(),
                report.getJewelleryProduct()
        );

        this.addSizeRing(report, jewelry);

        report.getClient().addJewelry(jewelry);

    }

    @Transactional
    private void create(Jewelry jewelry) {
        jewelryRepository.save(jewelry);
    }

    private Jewelry checkoutJewelryOrCreate(String article, JewelleryProduct jewelleryProduct) {
        try {
            return read(article);
        } catch (HttpException e) {
            Jewelry jewelry = new Jewelry(article, jewelleryProduct);
            this.create(jewelry);
            return jewelry;
        }
    }

    private void addSizeRing(Report report, Jewelry jewelry) {
        Resize resize = sizeRingService
                .checkoutSizeRingOrCreate(report.getSizeBefore(), report.getSizeAfter());
        jewelry.addSizeRing(resize);
    }
}
