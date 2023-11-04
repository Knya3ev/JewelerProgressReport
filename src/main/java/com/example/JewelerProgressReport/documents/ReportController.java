package com.example.JewelerProgressReport.documents;


import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.documents.response.ReportModeration;
import com.example.JewelerProgressReport.documents.response.ReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@Slf4j
public class ReportController {
    private final ReportService reportService;
    private final ReportMapper reportMapper;

    @Operation(summary = "Creating report")
    @PostMapping(value = "/{personId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportResponse> create(@PathVariable("personId") Long personId,
                                                 @RequestBody @Valid ReportRequest reportRequest) {
        return ResponseEntity.ok().body(reportMapper.toReportResponse(reportService.create(personId, reportRequest)));
    }

    @Operation(summary = "Update report ")
    @PatchMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable("id") Long id,
                                       @RequestBody @Valid ReportRequest reportRequest) {
        reportService.update(reportRequest, id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get report by id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportResponse> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(reportMapper.toReportResponse(reportService.read(id)));
    }

    @Operation(summary = "Get all reports")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReportResponse>> getAll() {
        return ResponseEntity.ok().body(reportMapper.toReportResponse(reportService.readAll()));
    }
    @Operation(summary = "Delete report")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reportService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all reports that are being moderated")
    @GetMapping(value = "/all/moderation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReportModeration>> getAllModerationReport() {
        return ResponseEntity.ok(reportMapper.toReportModeration(reportService.readAllModeration()));
    }

    @Operation(summary = "Get all reports that are unique")
    @GetMapping(value = "/all/uniqueness", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReportModeration>> getAllUniquenessReport() {
        return ResponseEntity.ok(reportMapper.toReportModeration(reportService.readAllUniqueness()));
    }

    @Operation(summary = "Get all reports that are rejection")
    @GetMapping(value = "/all/rejection", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReportModeration>> getAllRejectionReport() {
        return ResponseEntity.ok(reportMapper.toReportModeration(reportService.readAllRejection()));
    }

    @Operation(summary = "Get all reports that are normal")
    @GetMapping(value = "/all/ordinary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReportModeration>> getAllOrdinaryReport() {
        return ResponseEntity.ok(reportMapper.toReportModeration(reportService.readAllOrdinary()));
    }

    @Operation(summary = "confirmation that the product is unique or commonplace ")
    @PostMapping(value = "/{reportId}/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportModeration> approveReport(@PathVariable("reportId") Long reportId,
                                                          @RequestParam("unique") boolean unique) {
        return ResponseEntity.ok(reportService.approveReportResize(reportId, unique));
    }

    @Operation(summary = "refusal to write to the knowledge base , the record will be displayed only for the user ")
    @PostMapping(value = "/{reportId}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportModeration> approveReport(@PathVariable("reportId") Long reportId) {
        return ResponseEntity.ok(reportService.cancelReportResize(reportId));
    }

}
