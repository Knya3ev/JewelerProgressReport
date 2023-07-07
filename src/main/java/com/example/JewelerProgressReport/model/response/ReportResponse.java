package com.example.JewelerProgressReport.model.response;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ReportResponse {
    private Long id;
    private String typeProduct;
    private String typeOfMetalColor;
    private String typeOfOperation;
    private String phoneNumber;
    private String detailsOfOperation;
    private String unionCodeJewelry;
    private String article;
    private Date date;
}
