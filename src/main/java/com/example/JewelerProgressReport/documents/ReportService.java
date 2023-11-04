package com.example.JewelerProgressReport.documents;

import com.example.JewelerProgressReport.documents.enums.StatusReport;
import com.example.JewelerProgressReport.documents.response.ReportModeration;
import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.jewelry.jewelry_resize.JewelryResizeService;
import com.example.JewelerProgressReport.shop.response.ShopResponse;
import com.example.JewelerProgressReport.shop.response.ShopResponseFullCountStatus;
import com.example.JewelerProgressReport.users.client.Client;
import com.example.JewelerProgressReport.users.user.User;
import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.users.client.ClientService;
import com.example.JewelerProgressReport.jewelry.JewelryService;
import com.example.JewelerProgressReport.users.user.UserService;
import com.example.JewelerProgressReport.jewelry.resize.SizeRingService;
import jakarta.el.ELClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;
    private final ClientService clientService;
    private final UserService userService;
    private final JewelryService jewelryService;
    private final JewelryResizeService jewelryResizeService;
    private final SizeRingService sizeRingService;
    private final ReportMapper reportMapper;


    @Transactional
    public Report create(Long personId, ReportRequest reportRequest) {
        User user = userService.getUser(personId);
        Report report = reportMapper.toReport(reportRequest);

        user.addReport(report);

        if(user.getShop() != null) {
            user.getShop().getReports().add(report);
        }

        if (reportRequest.getArticle() != null) {
            boolean isHaveJewelry = jewelryResizeService.CheckoutUniqueJewelry(reportRequest);
            report.setStatus(isHaveJewelry? StatusReport.ORDINARY : StatusReport.MODERATION);
        }

        reportRepository.save(report);
        return report;
    }

    @Transactional
    public ReportModeration approveReportResize(Long reportId, boolean unique) {
        Report report = read(reportId);
        report.setStatus(unique? StatusReport.UNIQUE : StatusReport.ORDINARY);
        jewelryService.createJewelryIfIsNotNullArticle(report);
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


    @Transactional
    public void update(ReportRequest reportRequest, Long id) {
        Report reportUpdate = this.read(id);

        if (reportUpdate.getStatus().equals(StatusReport.UNIQUE)) {
            throw new HttpException("You cannot change the unique record", HttpStatus.BAD_REQUEST);
        }

        if(reportRequest.getArticle() != null
                && reportRequest.getSizeAfter() != null && reportRequest.getSizeBefore() != null){

            boolean isHaveJewelry = jewelryResizeService.CheckoutUniqueJewelry(reportRequest);
            reportUpdate.setStatus(isHaveJewelry? StatusReport.ORDINARY : StatusReport.MODERATION);
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
    public List<Report> readAllModeration() {
        return reportRepository.findAllByStatus(StatusReport.MODERATION.getCode());
    }

    public List<Report> readAllUniqueness() {
        return reportRepository.findAllByStatus(StatusReport.UNIQUE.getCode());
    }

    public List<Report> readAllRejection() {
        return reportRepository.findAllByStatus(StatusReport.REJECTION.getCode());
    }

    public List<Report> readAllOrdinary() {
        return reportRepository.findAllByStatus(StatusReport.ORDINARY.getCode());
    }

    public int getCountReportModeration(Long shopId){
        return reportRepository.countReportByStatus(shopId, StatusReport.MODERATION.getCode());
    }

    public int getCountReportUniqueness(Long shopId){
        return reportRepository.countReportByStatus(shopId, StatusReport.UNIQUE.getCode());
    }

    public int getCountReportRejection(Long shopId){
        return reportRepository.countReportByStatus(shopId, StatusReport.REJECTION.getCode());
    }

    public int getCountReportOrdinary(Long shopId){
        return reportRepository.countReportByStatus(shopId, StatusReport.ORDINARY.getCode());
    }
    public int getAllCount(Long shopId){
        return reportRepository.countAllResizes(shopId);
    }
}
