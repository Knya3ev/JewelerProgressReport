package com.example.JewelerProgressReport.documents.response;


import com.example.JewelerProgressReport.documents.enums.StatusReport;
import com.example.JewelerProgressReport.jewelry.enums.JewelleryOperation;
import com.example.JewelerProgressReport.jewelry.enums.JewelleryProduct;
import com.example.JewelerProgressReport.jewelry.enums.Metal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ReportModeration {

    private Long id;
    private JewelleryProduct jewelleryProduct;
    private Metal metal;
    private List<JewelleryOperation> jewelleryOperations;
    private String detailsOfOperation;
    private Double sizeBefore;
    private Double sizeAfter;
    private String unionCodeJewelry;
    private String article;
    private StatusReport status;
    private LocalDateTime createdDate;
    private boolean isEdit;
}
