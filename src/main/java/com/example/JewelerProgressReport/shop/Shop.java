package com.example.JewelerProgressReport.shop;


import com.example.JewelerProgressReport.users.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop", schema = "public")
public class Shop {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "shop_seq", sequenceName = "shop_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shop_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "number_of_administrators")
    private int numberOfAdministrators;

    @Column(name = "number_of_shop_assistant")
    private int numberOfShopAssistants;

    @Builder.Default
    @Column(name = "number_of_jeweler_master")
    private int numberOfJewelerMasters = 0;

    @Builder.Default
    @Column(name = "is_have_jeweler")
    private boolean isHaveJeweler = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private User director;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @Builder.Default
    private List<User> staff = new ArrayList<>();

    @Column(name = "administrators")
    private String administrators;

    @Column(name = "shop_assistants")
    private String shopAssistants;

    @Column(name = "jeweler_masters")
    private String jewelerMasters;


    @Column(name = "paid_subscription_validity_period")
    private LocalDateTime paidSubscriptionValidityPeriod;

    public List<Long> getAdministrators() {
        if(administrators == null) return new ArrayList<>();
        return Arrays.stream(administrators.split(";")).map(Long::parseLong).toList();
    }

    public void setAdministrators(List<Long> administrators) {
        this.administrators = administrators.stream().map(String::valueOf).collect(Collectors.joining(";"));
    }

    public List<Long> getShopAssistants() {
        if(shopAssistants == null) return new ArrayList<>();
        return Arrays.stream(shopAssistants.split(";")).map(Long::parseLong).toList();
    }

    public void setShopAssistants(List<Long> shopAssistants) {
        this.shopAssistants = shopAssistants.stream().map(String::valueOf).collect(Collectors.joining(";"));;
    }

    public List<Long> getJewelerMasters() {
        if(jewelerMasters == null) return new ArrayList<>();
        return Arrays.stream(jewelerMasters.split(";")).map(Long::parseLong).toList();
    }

    public void setJewelerMasters(List<Long> jewelerMasters) {
        this.jewelerMasters = jewelerMasters.stream().map(String::valueOf).collect(Collectors.joining(";"));;
    }
}
