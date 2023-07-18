package com.example.JewelerProgressReport.controllers;

import com.example.JewelerProgressReport.service.DocumentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;


    @GetMapping(value = "/loadCSV",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getReportCSV(@RequestParam(value = "personId") Long personId,
                                          @RequestParam(value = "month") int month,
                                          @RequestParam(value = "year", defaultValue = "0") int year){

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(documentService.getFileName(month,year)));
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(documentService.generationDocument(personId,month,year));
    }
}
