package com.example.JewelerProgressReport.documents;

import com.example.JewelerProgressReport.documents.enums.StatusReport;
import com.example.JewelerProgressReport.documents.request.ReportCounselingRequest;
import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.documents.response.ReportModeration;
import com.example.JewelerProgressReport.documents.response.ResponseCounseling;
import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.jewelry.JewelryService;
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
    private final ReportMapper reportMapper;

    @Transactional
    public Report create(Long personId, ReportRequest request) {
        User user = userService.getUser(personId);
        Report report = reportMapper.toReport(request);

        user.addReport(report);

        if (user.getShop() != null) {
            report.setShop(user.getShop());
        }

        if (request.getArticle() != null) {
            report.setStatus(
                    jewelryService.isUniqueJewelry(request)
                            ? StatusReport.MODERATION
                            : StatusReport.ORDINARY );

            clientService.addJewelry(
                    report.getClient().getNumberPhone(),
                    jewelryService.getOrCreate(report.getArticle(),report.getJewelleryProduct()));
        }

        reportRepository.save(report);
        return report;
    }
    @Transactional
    public ReportModeration approveReportResize(Long reportId, boolean unique) {
        Report report = read(reportId);

        if(report.getStatus().equals(StatusReport.UNIQUE)){
            throw new HttpException("This adjustment has already been approved", HttpStatus.BAD_REQUEST);
        }

        jewelryService.addSizeForJewelry(report);
        checkConsultation(report);

        report.setStatus(unique ? StatusReport.UNIQUE : StatusReport.ORDINARY);

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
        boolean  isUniqueJewelry = jewelryService.isUniqueJewelry(request);

        boolean isHaveConsultation = reportRepository.checkConsultation(
                request.getArticle(),
                request.getSizeBefore(),
                request.getSizeAfter(),
                StatusReport.CONSULTATION.getCode()).isPresent();

        if (!isUniqueJewelry || isHaveConsultation) {
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
    public void update(ReportRequest request, Long id) {
        Report reportUpdate = this.read(id);

        if (reportUpdate.getStatus().equals(StatusReport.UNIQUE)) {
            throw new HttpException("You cannot change the unique record", HttpStatus.BAD_REQUEST);
        }

        if (request.getArticle() != null && request.getSizeAfter() != null && request.getSizeBefore() != null) {
            reportUpdate.setStatus(
                    jewelryService.isUniqueJewelry(request)
                            ? StatusReport.MODERATION
                            : StatusReport.ORDINARY);
        }

        Report report = reportMapper.toReport(request);

        reportUpdate.setJewelleryProduct(report.getJewelleryProduct());
        reportUpdate.setMetal(report.getMetal());
        reportUpdate.setJewelleryOperations(report.getJewelleryOperations());
        reportUpdate.setDetailsOfOperation(report.getDetailsOfOperation());
        reportUpdate.setSizeBefore(request.getSizeBefore());
        reportUpdate.setSizeAfter(request.getSizeAfter());
        reportUpdate.setUnionCodeJewelry(report.getUnionCodeJewelry());
        reportUpdate.setArticle(report.getArticle());
        reportUpdate.setClient(clientService.checkoutClientOrCreate(request.getPhoneNumber(), true));
        reportUpdate.setEdit(true);
        reportUpdate.setEditDate(LocalDateTime.now());
    }

    public void delete(Long id) {
        Report report = this.read(id);

        if (report.getClient() != null) {
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
