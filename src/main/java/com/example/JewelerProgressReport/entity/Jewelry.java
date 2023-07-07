package com.example.JewelerProgressReport.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "jewelry")
@Entity
@NoArgsConstructor
public class Jewelry {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article")
    private String article;

    @Column(name = "image")
    private URI image;

    @Column(name = "type_jewelry")
    private String typeJewelry;

    @ManyToMany
    @JoinTable(name = "client_jewelry",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "jewelry_id"))
    private List<Client> clients = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "size_jewelry",
            joinColumns = @JoinColumn(name = "size_id"),
            inverseJoinColumns = @JoinColumn(name = "jewelry_id"))
    private List<SizeRing> sizeRings = new ArrayList<>();

    public Jewelry(String article, String typeJewelry) {
        this.article = article;
        this.typeJewelry = typeJewelry;
    }

    public void addSizeRing(SizeRing sizeRing){
        sizeRings.add(sizeRing);
        sizeRing.getJewelry().add(this);
    }
    public void removeSizeRing(SizeRing sizeRing){
        sizeRings.remove(sizeRing);
        sizeRing.getJewelry().remove(this);
    }
    // э0901кц02159700
    // 0901 - белое
    // 0301 - лимонное
    // 0201 -  красное
    // 1001 - комбенированное
}
