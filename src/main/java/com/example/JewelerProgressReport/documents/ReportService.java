package com.example.JewelerProgressReport.documents;

import com.example.JewelerProgressReport.documents.enums.StatusReport;
import com.example.JewelerProgressReport.documents.response.ReportModeration;
import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.users.client.Client;
import com.example.JewelerProgressReport.users.user.User;
import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.users.client.ClientService;
import com.example.JewelerProgressReport.jewelry.JewelryService;
import com.example.JewelerProgressReport.users.user.UserService;
import com.example.JewelerProgressReport.jewelry.resize.SizeRingService;
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
        User user = userService.getUser(personId);
        Report report = reportMapper.toReport(reportRequest);
        Client client = clientService.checkoutClientOrCreate(reportRequest.getPhoneNumber());

        client.addReports(report);
        user.addReport(report);

        if (reportRequest.getArticle() != null) {
            report.setStatus(StatusReport.MODERATION);
        }
        if(user.getShop() != null) {
            user.getShop().getReports().add(report);
        }
        reportRepository.save(report);
        return report;
    }

    @Transactional
    public ReportModeration approveReportResize(Long reportId, boolean unique) {
        Report report = read(reportId);

        if (unique) {
            report.setStatus(StatusReport.UNIQUE);
        } else {
            report.setStatus(StatusReport.ORDINARY);
        }
        return reportMapper.toReportModeration(report);
    }
    @Transactional
    public ReportModeration cancelReportResize(Long reportId) {
        Report report = read(reportId);

        report.setStatus(StatusReport.REJECTION);

        return reportMapper.toReportModeration(report);
    }

    public Report read(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new HttpException("Report by id %d not found".formatted(id), HttpStatus.NOT_FOUND));
    }

    public List<Report> readAll() {
        return reportRepository.findAll();
    }

    public List<Report> readAllModeration() {
        return reportRepository.findAllModeration(StatusReport.MODERATION.getCode());
    }

    public List<Report> readAllUniqueness() {
        return reportRepository.findAllModeration(StatusReport.UNIQUE.getCode());
    }

    public List<Report> readAllRejection() {
        return reportRepository.findAllModeration(StatusReport.REJECTION.getCode());
    }

    public List<Report> readAllOrdinary() {
        return reportRepository.findAllModeration(StatusReport.ORDINARY.getCode());
    }

    @Transactional
    public void update(ReportRequest reportRequest, Long id) {
        Report reportUpdate = this.read(id);

        if (reportUpdate.getStatus().equals(StatusReport.UNIQUE)) {
            throw new HttpException("You cannot change the unique record", HttpStatus.BAD_REQUEST);
        }

        if(reportRequest.getArticle() != null
                && reportRequest.getSizeAfter() != null && reportRequest.getSizeBefore() != null){
            reportUpdate.setStatus(StatusReport.MODERATION);
        }

        Report report = reportMapper.toReport(reportRequest);

        reportUpdate.setJewelleryProduct(report.getJewelleryProduct());
        reportUpdate.setMetal(report.getMetal());
        reportUpdate.setJewelleryOperations(report.getJewelleryOperations());
        reportUpdate.setDetailsOfOperation(report.getDetailsOfOperation());
        reportUpdate.setSizeBefore(reportRequest.getSizeBefore());
        reportUpdate.setSizeAfter(reportRequest.getSizeAfter());
        reportUpdate.setResize(sizeRingService.checkoutSizeRingOrCreate(report.getResize().getBefore(), report.getResize().getAfter()));
        reportUpdate.setUnionCodeJewelry(report.getUnionCodeJewelry());
        reportUpdate.setArticle(report.getArticle());
        reportUpdate.setClient(clientService.checkoutClientOrCreate(reportRequest.getPhoneNumber(), true));
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
