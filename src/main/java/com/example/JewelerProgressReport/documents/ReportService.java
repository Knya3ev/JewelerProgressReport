package com.example.JewelerProgressReport.documents;

import com.example.JewelerProgressReport.documents.enums.StatusReport;
import com.example.JewelerProgressReport.documents.request.ReportCounselingRequest;
import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.documents.response.ReportModeration;
import com.example.JewelerProgressReport.documents.response.ResponseCounseling;
import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.jewelry.JewelryService;
import com.example.JewelerProgressReport.jewelry.jewelry_resize.JewelryResizeService;
import com.example.JewelerProgressReport.jewelry.resize.SizeRingService;
import com.example.JewelerProgressReport.users.client.ClientService;
import com.example.JewelerProgressReport.users.user.User;
import com.example.JewelerProgressReport.users.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

        if (user.getShop() != null) {
            report.setShop(user.getShop());
        }

        if (reportRequest.getArticle() != null) {
            boolean isHaveJewelry = jewelryResizeService.CheckoutUniqueJewelry(reportRequest);
            report.setStatus(isHaveJewelry ? StatusReport.ORDINARY : StatusReport.MODERATION);
        }

        reportRepository.save(report);
        return report;
    }

    public ReportModeration approveReportResize(Long reportId, boolean unique) {
        Report report = read(reportId);

        if(report.getStatus().equals(StatusReport.UNIQUE)){
            throw new HttpException("This adjustment has already been approved", HttpStatus.BAD_REQUEST);
        }

        report.setStatus(unique ? StatusReport.UNIQUE : StatusReport.ORDINARY);
        jewelryService.createJewelryIfIsNotNullArticle(report);
        checkConsultation(report);

        return reportMapper.toReportModeration(report);
    }

    @Transactional
    public ReportModeration cancelReportResize(Long reportId) {
        Report report = read(reportId);
        report.setStatus(StatusReport.REJECTION);
        return reportMapper.toReportModeration(report);
    }

    @Transactional
    public ResponseCounseling createCounseling(Long userId, ReportCounselingRequest request) {
        boolean isHaveJewelry = jewelryResizeService.CheckoutUniqueJewelry(request);

        boolean isHaveConsultation = reportRepository.checkConsultation(
                request.getArticle(),
                request.getSizeBefore(),
                request.getSizeAfter(),
                StatusReport.CONSULTATION.getCode()).isPresent();

        if (isHaveJewelry || isHaveConsultation) {
            throw new HttpException(
                    "The jewelry already exists in the database, or a consultation for this product already exists ",
                    HttpStatus.BAD_REQUEST);
        }

        User user = userService.getUser(userId);
        Report report = reportMapper.toReport(request);

        user.addReport(report);

        if(user.getShop() != null){
            report.setShop(user.getShop());
        }
        reportRepository.save(report);
        return reportMapper.toResponseCounseling(report);
    }

    public void checkConsultation(Report reportRequest) {
        Optional<Report> report = reportRepository.checkConsultation(
                reportRequest.getArticle(),
                reportRequest.getSizeBefore(),
                reportRequest.getSizeAfter(),
                StatusReport.CONSULTATION.getCode());

        if(report.isPresent()){
            delete(report.get().getId());
        }
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

        if (reportRequest.getArticle() != null
                && reportRequest.getSizeAfter() != null && reportRequest.getSizeBefore() != null) {

            boolean isHaveJewelry = jewelryResizeService.CheckoutUniqueJewelry(reportRequest);
            reportUpdate.setStatus(isHaveJewelry ? StatusReport.ORDINARY : StatusReport.MODERATION);
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

    public void delete(Long id) {
        Report report = this.read(id);

        if (report.getClient() != null && report.getResize() != null) {
            report.removePersonAndClientAndResizes();
        }

        reportRepository.delete(report);
    }

    public List<ReportModeration> getAllReportByStatus(StatusReport status) {
        return reportMapper.toReportModeration(reportRepository.findAllByStatus(status.getCode()));
    }

    public int getCountReportsByStatusAndId(Long shopId, StatusReport status){
        return reportRepository.countReportByStatus(shopId,status.getCode());
    }

    public int getAllCount(Long shopId) {
        return reportRepository.countAllResizes(shopId);
    }
}
