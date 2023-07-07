package com.example.JewelerProgressReport.service;


import com.example.JewelerProgressReport.entity.Client;
import com.example.JewelerProgressReport.entity.Jewelry;
import com.example.JewelerProgressReport.entity.Report;
import com.example.JewelerProgressReport.entity.SizeRing;
import com.example.JewelerProgressReport.exception.JewelryNotFoundException;
import com.example.JewelerProgressReport.model.request.ReportRequest;
import com.example.JewelerProgressReport.repository.JewelryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JewelryService {
    private final JewelryRepository jewelryRepository;
    private final SizeRingService sizeRingService;

    public Jewelry read (String article){
        return jewelryRepository.findByArticle(article)
                .orElseThrow(() -> new JewelryNotFoundException("Jewelry with the article %s is not found".formatted(article)));
    }
    public void createJewelryIfIsNotNullArticle(Client client, ReportRequest reportRequest) {
        if (reportRequest.getArticle() != null && reportRequest.getSizeBefore() != null
                && reportRequest.getSizeAfter() != null) {

            Jewelry jewelry = this.checkoutJewelryOrCreate(reportRequest.getArticle(), reportRequest.getTypeProduct());

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
        }catch (JewelryNotFoundException e){
            Jewelry jewelry = new Jewelry(article,typeJewelry);
            this.create(jewelry);
            return jewelry;
        }
    }
    private void addSizeRing(ReportRequest report, Jewelry jewelry ){
        SizeRing sizeRing = sizeRingService
                .checkoutSizeRingOrCreate(report.getSizeAfter(),report.getSizeBefore());
        jewelry.addSizeRing(sizeRing);
    }
}
