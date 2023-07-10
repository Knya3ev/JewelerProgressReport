package com.example.JewelerProgressReport.model.request;


import com.example.JewelerProgressReport.model.typeEnum.TypeOfJewelry;
import com.example.JewelerProgressReport.util.validation.custom_annotation.TypeMetalColor;
import com.example.JewelerProgressReport.util.validation.custom_annotation.TypeOperation;
import com.example.JewelerProgressReport.util.validation.custom_annotation.TypeProduct;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class ReportRequest {

    @TypeProduct
    private String typeProduct;

    @TypeMetalColor
    private String typeOfMetalColor;

    @TypeOperation
    private List<String> typeOfOperation;

    @NotNull(message = "the phone field cannot be empty")
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$", message = "phone not correct")
    private String phoneNumber;
    private int count = 1;
    private String detailsOfOperation;
    private Double sizeBefore;
    private Double sizeAfter;
    private String unionCodeJewelry;
    private String article;
}
