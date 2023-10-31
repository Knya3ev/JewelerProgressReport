package com.example.JewelerProgressReport.jewelry.goldPrice;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gold-client", url = "${gold.api.url}", configuration = GoldPriceApiConfiguration.class)
public interface GoldPriceClient {
    @GetMapping("/{symbol}/{currency}")
    ResponseGold getGoldPrice(@PathVariable("symbol") String symbol,
                              @PathVariable("currency") String currency);

}
