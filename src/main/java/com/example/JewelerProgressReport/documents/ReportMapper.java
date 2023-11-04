package com.example.JewelerProgressReport.documents;

import com.example.JewelerProgressReport.documents.enums.StatusReport;
import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.documents.response.ReportModeration;
import com.example.JewelerProgressReport.documents.response.ReportResponse;
import com.example.JewelerProgressReport.jewelry.enums.JewelleryOperation;
import com.example.JewelerProgressReport.jewelry.enums.JewelleryProduct;
import com.example.JewelerProgressReport.jewelry.enums.Metal;
import com.example.JewelerProgressReport.jewelry.resize.SizeRingService;
import com.example.JewelerProgressReport.users.client.ClientService;
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
    private final ClientService clientService;

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

                .sizeBefore(reportRequest.getSizeBefore())
                .sizeAfter(reportRequest.getSizeAfter())
                .unionCodeJewelry(reportRequest.getUnionCodeJewelry())
                .count(reportRequest.getCount())
                .article(reportRequest.getArticle())
                .status(StatusReport.SERVICE)
                .client(clientService.checkoutClientOrCreate(reportRequest.getPhoneNumber()))
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


    public List<ReportResponse> toReportResponse(List<Report> reportList) {
        if (reportList == null) {
            return null;
        }

        List<ReportResponse> list = new ArrayList<ReportResponse>(reportList.size());
        for (Report report : reportList) {
            list.add(toReportResponse(report));
        }

        return list;
    }

    public ReportModeration toReportModeration(Report report){
        if (report == null) {
            return null;
        }
        return ReportModeration.builder()
                .id(report.getId())
                .article(report.getArticle())
                .metal(report.getMetal())
                .jewelleryProduct(report.getJewelleryProduct())
                .createdDate(report.getCreatedDate())
                .detailsOfOperation(report.getDetailsOfOperation())
                .jewelleryOperations(report.getJewelleryOperations())
                .sizeAfter(report.getSizeAfter())
                .sizeBefore(report.getSizeBefore())
                .status(report.getStatus())
                .isEdit(report.isEdit())
                .unionCodeJewelry(report.getUnionCodeJewelry())
                .build();
    }

    public List<ReportModeration> toReportModeration(List<Report> reportList){
        if (reportList == null) {
            return null;
        }

        List<ReportModeration> list = new ArrayList<ReportModeration>(reportList.size());
        for (Report report : reportList) {
            list.add(toReportModeration(report));
        }

        return list;
    }
}
