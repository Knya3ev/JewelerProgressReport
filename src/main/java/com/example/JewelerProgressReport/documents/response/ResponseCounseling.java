package com.example.JewelerProgressReport.documents.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCounseling {
    private Long id;
    private String article;
    private String singResize;
    private String nameShop;
}
