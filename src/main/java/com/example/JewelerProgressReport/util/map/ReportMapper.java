package com.example.JewelerProgressReport.util.map;

import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.documents.request.ReportRequest;
import com.example.JewelerProgressReport.documents.response.ReportResponse;
import com.example.JewelerProgressReport.jewelry.enums.TypeOfJewelry;
import com.example.JewelerProgressReport.jewelry.enums.TypeOfMetalColor;
import com.example.JewelerProgressReport.jewelry.enums.TypeOfOperation;
import com.example.JewelerProgressReport.jewelry.SizeRingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.JewelerProgressReport.jewelry.enums.TypeOfOperation.*;

@Service
@RequiredArgsConstructor
public class ReportMapper {

    private final SizeRingService sizeRingService;

    public Report toReport(ReportRequest reportRequest) {

        List<String> standardOperation = this.getStandardOperation(reportRequest.getTypeOfMetalColor());
        standardOperation.addAll(0, reportRequest.getTypeOfOperation());


        List<String> newList = standardOperation.stream()
                .map(operation -> TypeOfOperation.valueOf(operation).getRu())
                .collect(Collectors.toList());


        return Report.builder()
                .typeProduct(TypeOfJewelry.valueOf(reportRequest.getTypeProduct()).getRu())
                .typeOfMetalColor(TypeOfMetalColor.valueOf(reportRequest.getTypeOfMetalColor()).getRu())
                .typeOfOperation(String.join(", ", newList))
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
        reportResponse.setTypeProduct(report.getTypeProduct());
        reportResponse.setTypeOfMetalColor(report.getTypeOfMetalColor());
        reportResponse.setTypeOfOperation(report.getTypeOfOperation());
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

    private List<String> getStandardOperation(String metalColor) {
        TypeOfMetalColor typeOfMetalColor = TypeOfMetalColor.valueOf(metalColor);

        if (typeOfMetalColor == TypeOfMetalColor.white) {
            return new ArrayList<>(Arrays.asList(cleaning.get(), polishing.get(),
                    rhodiumPlating.get()));
        } else return new ArrayList<>(Arrays.asList(cleaning.get(), polishing.get()));
    }
}
