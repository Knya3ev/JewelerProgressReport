package com.example.JewelerProgressReport.util.map;


import com.example.JewelerProgressReport.entity.Report;
import com.example.JewelerProgressReport.model.request.ReportRequest;
import com.example.JewelerProgressReport.model.response.ReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ReportMapper {
    ReportResponse toReportResponse(Report report);
    List<ReportResponse> toReportResponseList(List<Report> reportList);

}
