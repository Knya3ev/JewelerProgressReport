package com.example.JewelerProgressReport.controllers;


import com.example.JewelerProgressReport.model.request.ReportRequest;
import com.example.JewelerProgressReport.util.map.ReportMapper;
import com.example.JewelerProgressReport.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final ReportMapper reportMapper;

    @Operation(summary = "Creating report")
    @PostMapping("/{personId}")
    public ResponseEntity<?> create(@PathVariable("personId") Long personId,
                                    @RequestBody @Valid ReportRequest reportRequest) {
        reportService.create(personId, reportRequest);
        return ResponseEntity.ok().body(HttpStatus.CREATED);
    }
    @Operation(summary = "Get report by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(reportMapper.toReportResponse(reportService.read(id)));
    }
    @Operation(summary = "Get all reports")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(reportMapper.toReportResponseList(reportService.readAll()));
    }
    @Operation(summary = "Update report ")
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @RequestBody @Valid ReportRequest reportRequest) {
        reportService.update(reportRequest, id);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
    @Operation(summary = "Delete report")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        reportService.delete(id);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
