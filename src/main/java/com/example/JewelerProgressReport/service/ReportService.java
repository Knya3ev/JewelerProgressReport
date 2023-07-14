package com.example.JewelerProgressReport.service;

import com.example.JewelerProgressReport.entity.Client;
import com.example.JewelerProgressReport.entity.Person;
import com.example.JewelerProgressReport.entity.Report;
import com.example.JewelerProgressReport.exception.ReportNotFoundException;
import com.example.JewelerProgressReport.model.request.ReportRequest;
import com.example.JewelerProgressReport.repository.ReportRepository;
import com.example.JewelerProgressReport.util.map.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ClientService clientService;
    private final PersonService personService;
    private final JewelryService jewelryService;

    private final SizeRingService sizeRingService;

    private final ReportMapper reportMapper;


    @Transactional
    public void create(Long personId, ReportRequest reportRequest) {
        Person person = personService.read(personId);
        Report report = reportMapper.toReport(reportRequest);
        Client client = clientService.checkoutClientOrCreate(reportRequest.getPhoneNumber());

        client.addReports(report);
        person.addReport(report);

        jewelryService.createJewelryIfIsNotNullArticle(client, reportRequest);

        reportRepository.save(report);
    }


    public Report read(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException(String.format("Report by id %d not found", id)));
    }

    public List<Report> readAll() {
        return reportRepository.findAll();
    }


    @Transactional
    public void update(ReportRequest reportRequest, Long id) {
        Report reportUpdate = this.read(id);

        Report report = reportMapper.toReport(reportRequest);

        reportUpdate.setTypeProduct(report.getTypeProduct());
        reportUpdate.setTypeOfMetalColor(report.getTypeOfMetalColor());
        reportUpdate.setTypeOfOperation(report.getTypeOfOperation());
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
