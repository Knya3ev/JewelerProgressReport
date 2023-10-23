package com.example.JewelerProgressReport.shop;

import com.example.JewelerProgressReport.shop.request.ShopRequest;
import com.example.JewelerProgressReport.shop.response.ShopResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class ShopController {
    private final ShopService shopService;

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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponse> create(@PathVariable("userId") Long userId,
                                               @RequestBody @Valid ShopRequest shopRequest) {
        return ResponseEntity.ok(shopService.createShop(shopRequest, userId));
    }

    @Operation(summary = "Add admin for shop")
    @PostMapping(value = "/add-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponse> addAdmin(@RequestParam("shopId") Long shopId,
                                                 @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(shopService.addAdmin(shopId, userId));
    }

    @Operation(summary = "Add shop assistants for shop")
    @PostMapping(value = "/add-shop-assistants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponse> addShopAssistants(@RequestParam("shopId") Long shopId,
                                                          @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(shopService.addShopAssistants(shopId, userId));
    }

    @Operation(summary = "Add jeweler for shop")
    @PostMapping(value = "/add-jeweler", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopResponse> addJeweler(@RequestParam("shopId") Long shopId,
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
    @PostMapping(value = "/{shopId}/edit/admin")
    public ResponseEntity<ShopResponse> editCountAdmin(@PathVariable("shopId") Long shopId,
                                                       @RequestParam("count") int count) {
        return ResponseEntity.ok(shopService.editCountAdmin(shopId, count));
    }

    @Operation(summary = "change the number of shop assistants for the shop")
    @PostMapping(value = "/{shopId}/edit/shop-assistants")
    public ResponseEntity<ShopResponse> editCountShopAssistants(@PathVariable("shopId") Long shopId,
                                                                @RequestParam("count") int count) {
        return ResponseEntity.ok(shopService.editCountShopAssistants(shopId, count));
    }

    @Operation(summary = "change the number of jeweler for the shop")
    @PostMapping(value = "/{shopId}/edit/jeweler")
    public ResponseEntity<ShopResponse> editCountJeweler(@PathVariable("shopId") Long shopId,
                                                         @RequestParam("count") int count) {
        return ResponseEntity.ok(shopService.editCountJeweler(shopId, count));
    }
}
