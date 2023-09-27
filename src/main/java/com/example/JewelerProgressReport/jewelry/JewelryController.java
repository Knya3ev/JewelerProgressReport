package com.example.JewelerProgressReport.jewelry;

import com.example.JewelerProgressReport.jewelry.response.JewelryResponse;
import com.example.JewelerProgressReport.util.map.JewelryMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jewelry")
@RequiredArgsConstructor
public class JewelryController {
    private final JewelryService jewelryService;
    private final JewelryMapper jewelryMapper;


    @GetMapping()
    @Operation(summary = "Get Jewelry and her size adjustment")
    public ResponseEntity<JewelryResponse> getByArticle(@RequestParam("article") String article){
        return ResponseEntity.ok(jewelryMapper.toJewelryResponse(jewelryService.read(article)));
    }

}
