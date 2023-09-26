package com.example.JewelerProgressReport.documents;


import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.documents.response.ReportResponse;
import com.example.JewelerProgressReport.util.map.ReportMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final ReportMapper reportMapper;

    @Operation(summary = "Creating report")
    @PostMapping("/{personId}")
    public ResponseEntity<ReportResponse> create(@PathVariable("personId") Long personId,
                                                 @RequestBody @Valid ReportRequest reportRequest) {
        return ResponseEntity.ok().body(reportMapper.toReportResponse(reportService.create(personId, reportRequest)));
    }
    @Operation(summary = "Get report by id")
    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(reportMapper.toReportResponse(reportService.read(id)));
    }
    @Operation(summary = "Get all reports")
    @GetMapping("/all")
    public ResponseEntity<List<ReportResponse>> getAll() {
        return ResponseEntity.ok().body(reportMapper.toReportResponseList(reportService.readAll()));
    }
    @Operation(summary = "Update report ")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id,
                                    @RequestBody @Valid ReportRequest reportRequest) {
        reportService.update(reportRequest, id);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Delete report")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        reportService.delete(id);
        return ResponseEntity.ok().build();
    }
}
