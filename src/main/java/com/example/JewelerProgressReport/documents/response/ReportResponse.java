package com.example.JewelerProgressReport.documents.response;
import lombok.*;

import java.util.Date;

@Getter
@Setter
public class ReportResponse {
    private Long id;
    private String jewelleryProduct;
    private String metal;
    private String jewelleryOperation;
    private String phoneNumber;
    private String detailsOfOperation;
    private String resize;
    private String unionCodeJewelry;
    private String article;
    private int count;
    private Date createdDate;
}
