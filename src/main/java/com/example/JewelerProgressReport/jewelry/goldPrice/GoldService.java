package com.example.JewelerProgressReport.jewelry.goldPrice;

import com.example.JewelerProgressReport.config.SettingProperties;
import com.example.JewelerProgressReport.jewelry.enums.Metal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoldService {
    private final GoldPriceClient goldPriceClient;
    private final SettingProperties setting;

    private final Map<String, Price> priceHash = new HashMap<>();
    @Value("${gold.price.rub}")
    private double PRICE_GOLD;
    @Value("${gold.api.time-out}")
    private int TIME_OUT;
    @Value("${gold.percentage.up}")
    private int PERCENTAGE_ADDED_UP_VALUE;
    @Value("${gold.percentage.medium}")
    private int PERCENTAGE_ADDED_MEDIUM_VALUE;
    @Value("${gold.percentage.low}")
    private int PERCENTAGE_ADDED_LOW_VALUE;
    private final double SPECIFIC_DENSITY_RED_GOLD_585 = 13.24;
    private final double SPECIFIC_DENSITY_WHITE_GOLD_585 = 12.85;
    private final double SPECIFIC_DENSITY_YELLOW_GOLD_585 = 13.6;


    public BigDecimal getPriceForAddingGold(double length, double width, double height, Metal metal) {

        double volume = (length * width * height) / 1000;

        double gramsOfMetal = volume * getMetal(metal);

        double priceGold = gramsOfMetal * getPriceGold();

        BigDecimal finalPrice = BigDecimal.valueOf(priceGold * (1 + getPercentageValue(length) / 100));

        return finalPrice.setScale(0, RoundingMode.HALF_DOWN);
    }

    private double getMetal(Metal metal) {
        switch (metal) {
            case WHITE_GOLD:
                return SPECIFIC_DENSITY_WHITE_GOLD_585;

            case YELLOW_GOLD:
                return SPECIFIC_DENSITY_YELLOW_GOLD_585;

            case RED_GOLD:
                return SPECIFIC_DENSITY_RED_GOLD_585;

            default:
                throw new IllegalStateException("Unexpected value: " + metal.name());
        }
    }

    private int getPercentageValue(double size) {
        if (size <= 3.5) {
            return PERCENTAGE_ADDED_UP_VALUE;
        }
        if (size > 3.5 && size <= 7.5) {
            return PERCENTAGE_ADDED_MEDIUM_VALUE;
        }
        if (size > 7.5) {
            return PERCENTAGE_ADDED_LOW_VALUE;
        } else {
            return PERCENTAGE_ADDED_UP_VALUE;
        }
    }

    private double getPriceGold() {
        String keyMetal = "price_gram_14k";
        LocalDateTime now = LocalDateTime.now(ZoneId.of(setting.getTimeZone()));

        if(priceHash.containsKey(keyMetal)) {
            if (now.isAfter(priceHash.get(keyMetal).data())) {
                return getData(keyMetal);
            }
            return priceHash.get(keyMetal).price();
        }
        return getData(keyMetal);
    }

    private double getData(String keyMetal){
        ResponseGold data = goldPriceClient.getGoldPrice("XAU", "RUB");

        Price price = new Price(
                data.getPrice_gram_14k(),
                LocalDateTime.now(ZoneId.of(setting.getTimeZone())).plusHours(TIME_OUT));

        priceHash.put(keyMetal, price);

        return price.price();
    }
}
