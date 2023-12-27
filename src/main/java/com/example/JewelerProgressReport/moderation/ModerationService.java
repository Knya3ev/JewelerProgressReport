package com.example.JewelerProgressReport.moderation;

import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.documents.ReportRepository;
import com.example.JewelerProgressReport.documents.ReportService;
import com.example.JewelerProgressReport.documents.enums.StatusReport;
import com.example.JewelerProgressReport.jewelry.JewelryRepository;
import com.example.JewelerProgressReport.moderation.response.CountAllResponse;
import com.example.JewelerProgressReport.shop.Shop;
import com.example.JewelerProgressReport.shop.ShopRepository;
import com.example.JewelerProgressReport.shop.ShopService;
import com.example.JewelerProgressReport.shop.response.ShopResponseCountModerationReports;
import com.example.JewelerProgressReport.shop.response.ShopResponseModeration;
import com.example.JewelerProgressReport.shop.response.ShopShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModerationService {

    private final ShopRepository shopRepository;
    private final ShopService shopService;
    private final ReportRepository reportRepository;
    private final ReportService reportService;


    public CountAllResponse getCountModerationAll() {
        return CountAllResponse.builder()
                .jewelry(getCountModerationJewelry())
                .shop(getCountModerationShop())
                .build();
    }

    private int getCountModerationJewelry() {
        return reportRepository.countAllByStatus(StatusReport.MODERATION.getCode());
    }

    private int getCountModerationShop() {
        return shopRepository.countAllModeration();
    }



    public List<ShopResponseCountModerationReports> getCountModerationForShop() {
        return shopRepository.findAll()
                .stream().map(this::toShopResponseCountModerationReports).toList();
    }

    private ShopResponseCountModerationReports toShopResponseCountModerationReports(Shop shop) {
        return ShopResponseCountModerationReports.builder()
                .id(shop.getId())
                .name(shop.getName())
                .moderation(reportService.getCountReportsByStatusAndId(shop.getId(), StatusReport.MODERATION))
                .build();
    }
}
