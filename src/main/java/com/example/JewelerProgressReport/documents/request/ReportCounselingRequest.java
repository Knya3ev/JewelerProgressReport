package com.example.JewelerProgressReport.documents.request;

import com.example.JewelerProgressReport.util.validation.custom_annotation.TypeMetalColor;
import com.example.JewelerProgressReport.util.validation.custom_annotation.TypeProduct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportCounselingRequest {
    @TypeProduct
    private String jewelleryProduct;
    @TypeMetalColor
    private String metal;
    private Double sizeBefore;
    private Double sizeAfter;
    private String article;
}
