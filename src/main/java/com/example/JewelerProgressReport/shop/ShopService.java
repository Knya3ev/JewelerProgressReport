package com.example.JewelerProgressReport.shop;

import com.example.JewelerProgressReport.config.SettingProperties;
import com.example.JewelerProgressReport.documents.ReportService;
import com.example.JewelerProgressReport.documents.enums.StatusReport;
import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.shop.request.ShopRequest;
import com.example.JewelerProgressReport.shop.response.ShopResponse;
import com.example.JewelerProgressReport.shop.response.ShopResponseCountModerationReports;
import com.example.JewelerProgressReport.shop.response.ShopResponseFullCountStatus;
import com.example.JewelerProgressReport.users.user.User;
import com.example.JewelerProgressReport.users.user.UserService;
import com.example.JewelerProgressReport.util.map.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;
    private final UserService userService;
    private final ReportService reportService;
    private final SettingProperties settingProperties;
    private final UserMapper userMapper;

    @Value("${shop.trial-subscription-days}")
    private int numberOfTrialSubscriptionDays;

    private Shop getShop(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new HttpException("Shop not found by id: %d".formatted(id), HttpStatus.NOT_FOUND));
    }

    public ShopResponse getShopResponse(Long id) {
        return toResponse(getShop(id));
    }

    public List<ShopResponse> getAllShops() {
        return shopRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public ShopResponse createShop(ShopRequest shopRequest, Long userId) {
        User director = userService.getUser(userId);

        if (director.getCountShops() == 0) {
            throw new HttpException("You can't create a shop, your limit for creating shops: %d"
                    .formatted(director.getCountShops()), HttpStatus.BAD_REQUEST);
        }

        if (director.getCountShops() > director.getShopOwnership().size()) {

            if (shopRepository.findByName(shopRequest.getName().toLowerCase()).isPresent()) {
                throw new HttpException("a shop with the name '%s' already exists. "
                        .formatted(shopRequest.getName()), HttpStatus.BAD_REQUEST);
            }

            Shop shop = shopRepository.save(toShop(shopRequest, userId));
            director.getShopOwnership().add(shop);

            return toResponse(shop);
        }

        throw new HttpException("You can't create a shop, your limit for creating shops: %d"
                .formatted(director.getCountShops()), HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ShopResponse addAdmin(Long shopId, Long userId) {
        User admin = userService.getUser(userId);
        Shop shop = getShop(shopId);

        if (shop.getNumberOfAdministrators() > shop.getAdministrators().size()) {
            shop.setAdministrators(addForList(shop.getAdministrators(), admin.getId()));
            shop.getStaff().add(admin);
            return toResponse(shop);
        }
        throw new HttpException("You've got a limit on the number of administrators, no more: %d"
                .formatted(shop.getNumberOfAdministrators()), HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ShopResponse addShopAssistants(Long shopId, Long userId) {
        User shopAssistants = userService.getUser(userId);
        Shop shop = getShop(shopId);

        if (shop.getNumberOfShopAssistants() > shop.getShopAssistants().size()) {
            shop.setShopAssistants(addForList(shop.getShopAssistants(), shopAssistants.getId()));
            shop.getStaff().add(shopAssistants);
            return toResponse(shop);
        }
        throw new HttpException("You've got a limit on the number of shop assistants, no more: %d"
                .formatted(shop.getNumberOfShopAssistants()), HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ShopResponse addJeweler(Long shopId, Long userId) {
        Shop shop = getShop(shopId);

        if (shop.isHaveJeweler()) {
            User jeweler = userService.getUser(userId);

            if (shop.getNumberOfJewelerMasters() > shop.getJewelerMasters().size()) {
                shop.setJewelerMasters(addForList(shop.getJewelerMasters(), jeweler.getId()));
                shop.getStaff().add(jeweler);
                return toResponse(shop);
            }

            throw new HttpException("You've got a limit on the number of jeweler, no more: %d"
                    .formatted(shop.getNumberOfJewelerMasters()), HttpStatus.BAD_REQUEST);

        }

        throw new HttpException("You don't have the option of adding a jeweller ", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public void giveAccessToAddJeweller(Long shopId) {
        shopRepository.accessAddJeweler(shopId);
    }

    @Transactional
    public ShopResponse editCountAdmin(Long shopId, int count) {
        shopRepository.editCountAdmin(shopId, count);
        return toResponse(getShop(shopId));
    }

    @Transactional
    public ShopResponse editCountShopAssistants(Long shopId, int count) {
        shopRepository.editCountShopAssistants(shopId, count);
        return toResponse(getShop(shopId));
    }

    @Transactional
    public ShopResponse editCountJeweler(Long shopId, int count) {
        shopRepository.editCountJeweler(shopId, count);
        return toResponse(getShop(shopId));
    }

    public ShopResponseFullCountStatus getNumbersAllStatusesStore(Long shopId){

        return ShopResponseFullCountStatus.builder()
                .id(shopId)
                .name(getShop(shopId).getName())
                .all(reportService.getAllCount(shopId))
                .moderation(reportService.getCountReportsByStatusAndId(shopId, StatusReport.MODERATION))
                .uniqueness(reportService.getCountReportsByStatusAndId(shopId, StatusReport.UNIQUE))
                .ordinary(reportService.getCountReportsByStatusAndId(shopId, StatusReport.ORDINARY))
                .rejection(reportService.getCountReportsByStatusAndId(shopId, StatusReport.REJECTION))
                .build();

    }

    public List<ShopResponseCountModerationReports> getCountModerationForShop(){
        return shopRepository.findAll()
                .stream().map(this::toShopResponseCountModerationReports).toList();
    }

    private Shop toShop(ShopRequest request, Long userId) {

        return Shop.builder()
                .name(request.getName().toLowerCase())
                .director(userService.getUser(userId))
                .numberOfAdministrators(request.getNumberOfAdministrators())
                .numberOfShopAssistants(request.getNumberOfShopAssistants())
                .paidSubscriptionValidityPeriod(LocalDateTime
                        .now(ZoneId.of(settingProperties.getTimeZone())).plusDays(numberOfTrialSubscriptionDays))
                .build();
    }

    private ShopResponse toResponse(Shop shop) {
        LocalDateTime nowDate = shop.getPaidSubscriptionValidityPeriod();
        LocalDateTime targetLocalDate = LocalDateTime.now(ZoneId.of(settingProperties.getTimeZone()));
        LocalDateTime tempDateTime = LocalDateTime.from(nowDate);

        Integer days = Math.toIntExact(tempDateTime.until(targetLocalDate, ChronoUnit.DAYS)) * -1;

        return ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .director(userMapper.toUserResponse(shop.getDirector()))
                .administrators(shop.getAdministrators().stream().map(userService::getUserResponse).toList())
                .shopAssistants(shop.getShopAssistants().stream().map(userService::getUserResponse).toList())
                .isHaveJeweler(shop.isHaveJeweler())
                .jewelerMasters(shop.getJewelerMasters().stream().map(userService::getUserResponse).toList())
                .subscriptionDays(days)
                .build();
    }

    private ShopResponseCountModerationReports toShopResponseCountModerationReports(Shop shop){
        return ShopResponseCountModerationReports.builder()
                .id(shop.getId())
                .name(shop.getName())
                .moderation(reportService.getCountReportsByStatusAndId(shop.getId(), StatusReport.MODERATION))
                .build();
    }

    private List<Long> addForList(List<Long> list, Long element) {
        List<Long> mutableList = new ArrayList<>(list);
        mutableList.add(element);
        return mutableList;
    }


}
