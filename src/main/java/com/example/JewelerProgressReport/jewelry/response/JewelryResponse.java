package com.example.JewelerProgressReport.jewelry.response;

import com.example.JewelerProgressReport.jewelry.enums.JewelleryProduct;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JewelryResponse {
    private Long id;
    private String jewelleryProduct;
    private String article;
    private List<String> resizes;

}
