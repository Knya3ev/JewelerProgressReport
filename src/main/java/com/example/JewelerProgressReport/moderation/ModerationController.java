package com.example.JewelerProgressReport.moderation;


import com.example.JewelerProgressReport.moderation.response.CountAllResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/moderation")
@RequiredArgsConstructor
public class ModerationController {

    private final ModerationService moderationService;


    @Operation(summary = "Get count all moderation")
    @GetMapping(value = "/count/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountAllResponse> getCountModerationJewelryAndShop() {
        return ResponseEntity.ok(moderationService.getCountModerationAll());
    }

}
