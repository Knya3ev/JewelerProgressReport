package com.example.JewelerProgressReport.jewelry;

import com.example.JewelerProgressReport.jewelry.enums.Metal;
import com.example.JewelerProgressReport.jewelry.gold_price.GoldService;
import com.example.JewelerProgressReport.jewelry.response.JewelryResponse;
import com.example.JewelerProgressReport.util.map.JewelryMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/jewelry")
@RequiredArgsConstructor
public class JewelryController {
    private final JewelryService jewelryService;
    private final JewelryMapper jewelryMapper;
    private final GoldService goldService;


    @GetMapping()
    @Operation(summary = "Get Jewelry and her size adjustment")
    public ResponseEntity<JewelryResponse> getByArticle(@RequestParam("article") String article){
        return ResponseEntity.ok(jewelryMapper.toJewelryResponse(jewelryService.read(article)));
    }

    @GetMapping("/gold/price")
    public ResponseEntity<BigDecimal> getPriceGold(@RequestParam double length,
                                               @RequestParam double width,
                                               @RequestParam double height,
                                               @RequestParam String metal){

        return ResponseEntity.ok(goldService.getPriceForAddingGold(length,width,height,Metal.fromCode(metal)));
    }


}
