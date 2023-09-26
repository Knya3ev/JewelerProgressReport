package com.example.JewelerProgressReport.jewelry;


import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.jewelry.resize.Resize;
import com.example.JewelerProgressReport.jewelry.resize.SizeRingService;
import com.example.JewelerProgressReport.users.client.Client;
import com.example.JewelerProgressReport.documents.request.ReportRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JewelryService {
    private final JewelryRepository jewelryRepository;
    private final SizeRingService sizeRingService;

    public Jewelry read (String article){
        return jewelryRepository.findByArticle(article)
                .orElseThrow(() -> new HttpException("Jewelry with the article %s is not found".formatted(article), HttpStatus.NOT_FOUND));
    }
    public void createJewelryIfIsNotNullArticle(Client client, ReportRequest reportRequest) {
        if (reportRequest.getArticle() != null
                && reportRequest.getSizeBefore() != null
                && reportRequest.getSizeAfter() != null) {

            Jewelry jewelry = this.checkoutJewelryOrCreate(reportRequest.getArticle(), reportRequest.getJewelleryProduct());

            this.addSizeRing(reportRequest, jewelry);
            client.addJewelry(jewelry);
        }
    }

    @Transactional
    private void create(Jewelry jewelry){
        jewelryRepository.save(jewelry);
    }

    private Jewelry checkoutJewelryOrCreate(String article, String typeJewelry){
        try{
            return read(article);
        }catch (HttpException e){
            Jewelry jewelry = new Jewelry(article,typeJewelry);
            this.create(jewelry);
            return jewelry;
        }
    }
    private void addSizeRing(ReportRequest report, Jewelry jewelry ){
        Resize resize = sizeRingService
                .checkoutSizeRingOrCreate(report.getSizeBefore(),report.getSizeAfter());
        jewelry.addSizeRing(resize);
    }
}
