package com.example.JewelerProgressReport.shop;


import com.example.JewelerProgressReport.users.person.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "shop")
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
    @Column(name = "number_of_jeweler_master")
    private int numberOfJewelerMasters;
    @Column(name = "is_have_jeweler")
    private boolean isHaveJeweler = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person director;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private List<Person> administrators = new ArrayList<>();

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private List<Person> shopAssistants = new ArrayList<>();

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private List<Person> jewelerMasters = new ArrayList<>();

    @Column(name = "paid_subscription_validity_period")
    private LocalDateTime paidSubscriptionValidityPeriod;
}
