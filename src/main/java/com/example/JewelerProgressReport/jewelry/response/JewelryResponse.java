package com.example.JewelerProgressReport.jewelry.response;

import com.example.JewelerProgressReport.documents.response.ResponseCounseling;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JewelryResponse {
    private Long id;
    private String jewelleryProduct;
    private String article;
    private Double lowerLimit;
    private Double upperLimit;
    private List<String> resizes;
    private List<ResponseCounseling> consultations;
}
