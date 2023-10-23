package com.example.JewelerProgressReport.documents;

import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.users.client.Client;
import com.example.JewelerProgressReport.users.user.User;
import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.users.client.ClientService;
import com.example.JewelerProgressReport.jewelry.JewelryService;
import com.example.JewelerProgressReport.users.user.UserService;
import com.example.JewelerProgressReport.jewelry.resize.SizeRingService;
import com.example.JewelerProgressReport.util.map.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ClientService clientService;
    private final UserService userService;
    private final JewelryService jewelryService;

    private final SizeRingService sizeRingService;

    private final ReportMapper reportMapper;


    @Transactional
    public Report create(Long personId, ReportRequest reportRequest) {
        User user = userService.read(personId);
        Report report = reportMapper.toReport(reportRequest);
        Client client = clientService.checkoutClientOrCreate(reportRequest.getPhoneNumber());

        client.addReports(report);
        user.addReport(report);

        jewelryService.createJewelryIfIsNotNullArticle(client, reportRequest);

        reportRepository.save(report);
        return report;
    }


    public Report read(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new HttpException ("Report by id %d not found".formatted(id), HttpStatus.NOT_FOUND));
    }

    public List<Report> readAll() {
        return reportRepository.findAll();
    }


    @Transactional
    public void update(ReportRequest reportRequest, Long id) {
        Report reportUpdate = this.read(id);

        Report report = reportMapper.toReport(reportRequest);

        reportUpdate.setJewelleryProduct(report.getJewelleryProduct());
        reportUpdate.setMetal(report.getMetal());
        reportUpdate.setJewelleryOperations(report.getJewelleryOperations());
        reportUpdate.setDetailsOfOperation(report.getDetailsOfOperation());
        reportUpdate.setResize(sizeRingService.checkoutSizeRingOrCreate(report.getResize().getBefore(), report.getResize().getAfter()));
        reportUpdate.setUnionCodeJewelry(report.getUnionCodeJewelry());
        reportUpdate.setArticle(report.getArticle());
        reportUpdate.setClient(clientService.checkoutClientOrCreate(reportRequest.getPhoneNumber(),true));
        reportUpdate.setEdit(true);
        reportUpdate.setEditDate(LocalDateTime.now());
    }


    @Transactional
    public void delete(Long id) {
        Report report = this.read(id);
        report.removePersonAndClientAndResizes();
        reportRepository.delete(report);
    }

}
