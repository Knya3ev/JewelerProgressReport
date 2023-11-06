package com.example.JewelerProgressReport.shop;

import com.example.JewelerProgressReport.documents.ReportService;
import com.example.JewelerProgressReport.shop.request.ShopRequest;
import com.example.JewelerProgressReport.shop.response.ShopResponse;
import com.example.JewelerProgressReport.shop.response.ShopResponseCountModerationReports;
import com.example.JewelerProgressReport.shop.response.ShopResponseFullCountStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
@Slf4j
public class ShopController {
    private final ShopService shopService;
    private final ReportService reportService;

    @Operation(summary = "Get shop by id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponse> getShop(@PathVariable("id") Long id) {
        return ResponseEntity.ok(shopService.getShopResponse(id));
    }

    @Operation(summary = "Get all shops")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ShopResponse>> getAll() {
        return ResponseEntity.ok(shopService.getAllShops());
    }

    @Operation(summary = "Create shop")
    @PostMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponse> create(@PathVariable("userId") Long userId,
                                               @RequestBody @Valid ShopRequest shopRequest) {
        return ResponseEntity.ok(shopService.createShop(shopRequest, userId));
    }

    @Operation(summary = "Add admin for shop")
    @PostMapping(value = "/{shopId}/add-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponse> addAdmin(@PathVariable("shopId") Long shopId,
                                                 @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(shopService.addAdmin(shopId, userId));
    }

    @Operation(summary = "Add shop assistants for shop")
    @PostMapping(value = "/{shopId}/add-shop-assistants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponse> addShopAssistants(@PathVariable("shopId") Long shopId,
                                                          @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(shopService.addShopAssistants(shopId, userId));
    }

    @Operation(summary = "Add jeweler for shop")
    @PostMapping(value = "/{shopId}/add-jeweler", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponse> addJeweler(@PathVariable("shopId") Long shopId,
                                                   @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(shopService.addJeweler(shopId, userId));
    }

    @Operation(summary = "Grant permission to add a jeweller ")
    @PostMapping(value = "/{shopId}/jeweler-master/access")
    public ResponseEntity<Void> giveAccessToAddJeweller(@PathVariable("shopId") Long id) {
        shopService.giveAccessToAddJeweller(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "change the number of administrators for the shop")
    @PostMapping(value = "/{shopId}/edit/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponse> editCountAdmin(@PathVariable("shopId") Long shopId,
                                                       @RequestParam("count") int count) {
        return ResponseEntity.ok(shopService.editCountAdmin(shopId, count));
    }

    @Operation(summary = "change the number of shop assistants for the shop")
    @PostMapping(value = "/{shopId}/edit/shop-assistants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponse> editCountShopAssistants(@PathVariable("shopId") Long shopId,
                                                                @RequestParam("count") int count) {
        return ResponseEntity.ok(shopService.editCountShopAssistants(shopId, count));
    }

    @Operation(summary = "change the number of jeweler for the shop")
    @PostMapping(value = "/{shopId}/edit/jeweler", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponse> editCountJeweler(@PathVariable("shopId") Long shopId,
                                                         @RequestParam("count") int count) {
        return ResponseEntity.ok(shopService.editCountJeweler(shopId, count));
    }

    @Operation(summary = "Get numbers all statuses store")
    @GetMapping(value = "/{shopId}/full-counts")
    public ResponseEntity<ShopResponseFullCountStatus> getFullCountForShop(@PathVariable("shopId") Long shopId) {
        ShopResponseFullCountStatus shopResponseFullCountStatus = shopService.getNumbersAllStatusesStore(shopId);
        log.info(shopResponseFullCountStatus.toString());
        return ResponseEntity.ok(shopResponseFullCountStatus);
    }

    @Operation(summary = "Get count reports moderation for shops")
    @GetMapping(value = "/all/moderation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ShopResponseCountModerationReports>> getCountModerationForShops() {
        return ResponseEntity.ok(shopService.getCountModerationForShop());
    }

}
