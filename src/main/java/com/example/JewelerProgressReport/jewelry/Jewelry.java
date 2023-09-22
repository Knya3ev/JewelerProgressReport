package com.example.JewelerProgressReport.jewelry;


import com.example.JewelerProgressReport.users.client.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table(name = "jewelry")
@Entity
@NoArgsConstructor
public class Jewelry {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "jewelry_seq", sequenceName = "jewelry_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jewelry_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "article")
    private String article;

    @Column(name = "image")
    private URI image;

    @Column(name = "type_jewelry")
    private String typeJewelry;

    @ManyToMany
    @JoinTable(name = "jewelry_client",
            joinColumns = @JoinColumn(name = "jewelry_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private Set<Client> clients = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "jewelry_resize",
            joinColumns = @JoinColumn(name = "jewelry_id"),
            inverseJoinColumns = @JoinColumn(name = "resize_id"))
    private Set<Resize> resizes = new HashSet<>();

    public Jewelry(String article, String typeJewelry) {
        this.article = article;
        this.typeJewelry = typeJewelry;
    }

    public void addSizeRing(Resize resize) {
        this.resizes.add(resize);
        resize.getJewelry().add(this);
    }

    public void removeSizeRing(Resize resize) {
        this.resizes.remove(resize);
        resize.getJewelry().remove(this);
    }
    // э0901кц02159700
    // 02 - красное
    // 03 - лимонное
    // 09 - белое/ поладий
    // 10 - комбенированное
    // 40 - белое / платина
}
