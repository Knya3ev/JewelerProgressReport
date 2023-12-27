package com.example.JewelerProgressReport.shop;

import com.example.JewelerProgressReport.shop.request.ShopRequest;
import com.example.JewelerProgressReport.shop.response.ShopFullResponse;
import com.example.JewelerProgressReport.shop.response.ShopResponseCountModerationReports;
import com.example.JewelerProgressReport.shop.response.ShopResponseFullCountStatus;
import com.example.JewelerProgressReport.shop.response.ShopResponseModeration;
import com.example.JewelerProgressReport.shop.response.ShopShortResponse;
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

    @Operation(summary = "Get all shops short moderation")
    @GetMapping(value = "/all/moderation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ShopShortResponse>> getAllShopModeration() {
        return ResponseEntity.ok(shopService.getAllModeration());
    }

    @Operation(summary = "Get shop moderation by id")
    @GetMapping(value = "/{shopId}/moderation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponseModeration> getShopModerationById(@PathVariable("shopId") Long shopId) {
        return ResponseEntity.ok(shopService.getShopModeration(shopId));
    }
    @Operation(summary = "Get shop by id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopFullResponse> getShop(@PathVariable("id") Long id) {
        return ResponseEntity.ok(shopService.getShopResponse(id));
    }

    @Operation(summary = "Get all shops")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ShopFullResponse>> getAll() {
        return ResponseEntity.ok(shopService.getAllShops());
    }

    @Operation(summary = "Get all shops short verified")
    @GetMapping(value = "/all-short", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ShopShortResponse>> getAllShopVerified() {
        return ResponseEntity.ok(shopService.getAllVerified());
    }


    @Operation(summary = "Cancel request for adding in the shop")
    @GetMapping(value = "/request-for-adding/cancel")
    public ResponseEntity<Void> cancelRequestForAddingInTheShop() {
        shopService.cancelRequestForAddingInTheShop(false, null);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "director and admin  Cancel for user request for adding in the shop")
    @GetMapping(value = "/request-for-adding/reject/{userId}")
    public ResponseEntity<Void> rejectRequestForAddingInTheShop(@PathVariable("userId") Long userId) {
        shopService.cancelRequestForAddingInTheShop(true, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Request for adding in the shop")
    @GetMapping(value = "/{shopId}/request-for-adding", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopShortResponse> requestForAddingInTheShop(@PathVariable("shopId") Long shopId) {
        return ResponseEntity.ok(shopService.requestForAddingInTheShop(shopId));
    }

    @Operation(summary = "Create shop for User")
    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody @Valid ShopRequest shopRequest) {
        shopService.createShopForUser(shopRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Approve create shop for user")
    @PostMapping(value = "/{shopId}/approve")
    public ResponseEntity<Void> approve(@PathVariable("shopId") Long shopId) {
        shopService.approvalCreateShop(shopId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cancel create shop for user")
    @PostMapping(value = "/{shopId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("shopId") Long shopId) {
        shopService.cancelCreateShop(shopId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create shop for Director")
    @PostMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopFullResponse> createForDirector(@PathVariable("userId") Long userId,
                                                   @RequestBody @Valid ShopRequest shopRequest) {
        return ResponseEntity.ok(shopService.createShopForDirector(shopRequest, userId));
    }

    @Operation(summary = "Add admin for shop")
    @PostMapping(value = "/{shopId}/add-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopFullResponse> addAdmin(@PathVariable("shopId") Long shopId,
                                                     @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(shopService.addAdmin(shopId, userId));
    }

    @Operation(summary = "Add shop assistants for shop")
    @PostMapping(value = "/{shopId}/add-shop-assistants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopFullResponse> addShopAssistants(@PathVariable("shopId") Long shopId,
                                                              @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(shopService.addShopAssistants(shopId, userId));
    }

    @Operation(summary = "Add jeweler for shop")
    @PostMapping(value = "/{shopId}/add-jeweler", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopFullResponse> addJeweler(@PathVariable("shopId") Long shopId,
                                                       @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(shopService.addJeweler(shopId, userId));
    }


    @Operation(summary = "change the number of administrators for the shop")
    @PostMapping(value = "/{shopId}/edit/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopFullResponse> editCountAdmin(@PathVariable("shopId") Long shopId,
                                                           @RequestParam("count") int count) {
        return ResponseEntity.ok(shopService.editCountAdmin(shopId, count));
    }

    @Operation(summary = "change the number of shop assistants for the shop")
    @PostMapping(value = "/{shopId}/edit/shop-assistants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopFullResponse> editCountShopAssistants(@PathVariable("shopId") Long shopId,
                                                                    @RequestParam("count") int count) {
        return ResponseEntity.ok(shopService.editCountShopAssistants(shopId, count));
    }

    @Operation(summary = "Grant permission to add a jeweller ")
    @PostMapping(value = "/{shopId}/jeweler-master/access")
    public ResponseEntity<Void> giveAccessToAddJeweller(@PathVariable("shopId") Long id) {
        shopService.giveAccessToAddJeweller(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "change the number of jeweler for the shop")
    @PostMapping(value = "/{shopId}/edit/jeweler", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopFullResponse> editCountJeweler(@PathVariable("shopId") Long shopId,
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
    @GetMapping(value = "/all/moderation/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ShopResponseCountModerationReports>> getCountModerationForShops() {
        return ResponseEntity.ok(shopService.getCountModerationForShop());
    }

}
