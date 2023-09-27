package com.example.JewelerProgressReport.util.map;

import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.documents.response.ReportResponse;
import com.example.JewelerProgressReport.jewelry.enums.JewelleryProduct;
import com.example.JewelerProgressReport.jewelry.enums.Metal;
import com.example.JewelerProgressReport.jewelry.enums.JewelleryOperation;
import com.example.JewelerProgressReport.jewelry.resize.SizeRingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportMapper {

    private final SizeRingService sizeRingService;

    public Report toReport(ReportRequest reportRequest) {

        List<JewelleryOperation> operations = reportRequest.getJewelleryOperation()
                .stream().map(operation -> JewelleryOperation.fromCode(operation)).collect(Collectors.toList());

        //TODO: реализовать проверку if(item != консультация)

        operations.addAll(0, JewelleryOperation.getStandardOperation(reportRequest.getMetal()));

        return Report.builder()
                .jewelleryProduct(JewelleryProduct.fromCode(reportRequest.getJewelleryProduct()))
                .metal(Metal.fromCode(reportRequest.getMetal()))
                .jewelleryOperations(operations)
                .detailsOfOperation(reportRequest.getDetailsOfOperation())

                .resize(reportRequest.getSizeBefore() != null && reportRequest.getSizeAfter() != null ?
                        sizeRingService.checkoutSizeRingOrCreate(reportRequest.getSizeBefore(), reportRequest.getSizeAfter())
                        : null)

                .unionCodeJewelry(reportRequest.getUnionCodeJewelry())
                .count(reportRequest.getCount())
                .article(reportRequest.getArticle())
                .createdDate(LocalDateTime.now())
                .build();
    }


    public ReportResponse toReportResponse(Report report) {
        if (report == null) {
            return null;
        }

        ReportResponse reportResponse = new ReportResponse();

        reportResponse.setId(report.getId());
        reportResponse.setJewelleryProduct(report.getJewelleryProduct().getRu());
        reportResponse.setMetal(report.getMetal().getRu());
        reportResponse.setJewelleryOperation(
                String.join(", ", report.getJewelleryOperations().stream()
                        .map(operation -> operation.getRu())
                        .collect(Collectors.toList()))
        );
        reportResponse.setDetailsOfOperation(report.getDetailsOfOperation());
        reportResponse.setUnionCodeJewelry(report.getUnionCodeJewelry());
        reportResponse.setPhoneNumber(report.getClient().getNumberPhone());
        reportResponse.setResize(report.getResize() != null? report.getResize().getRingResizing(): null);
        reportResponse.setArticle(report.getArticle());
        reportResponse.setCount(report.getCount());
        if (report.getCreatedDate() != null) {
            reportResponse.setCreatedDate(Date.from(report.getCreatedDate().toInstant(ZoneOffset.UTC)));
        }

        return reportResponse;
    }


    public List<ReportResponse> toReportResponseList(List<Report> reportList) {
        if (reportList == null) {
            return null;
        }

        List<ReportResponse> list = new ArrayList<ReportResponse>(reportList.size());
        for (Report report : reportList) {
            list.add(toReportResponse(report));
        }

        return list;
    }
}
