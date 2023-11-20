package com.example.JewelerProgressReport.jewelry;


import com.example.JewelerProgressReport.jewelry.enums.JewelleryProduct;
import com.example.JewelerProgressReport.users.client.Client;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jewelry", schema = "public")
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
    private JewelleryProduct jewelleryProduct;

    @Column(name = "lower_limit")
    private Double lowerLimit;

    @Column(name = "upper_limit")
    private Double upperLimit;

    @ManyToMany()
    @JoinTable(name = "jewelry_client",
            joinColumns = @JoinColumn(name = "jewelry_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private Set<Client> clients = new HashSet<>();

    public Jewelry(String article, JewelleryProduct jewelleryProduct) {
        this.article = article;
        this.jewelleryProduct = jewelleryProduct;
    }

    // э0901кц02159700
    // 02 - красное
    // 03 - лимонное
    // 09 - белое/ поладий
    // 10 - комбенированное
    // 40 - белое / платина
}   // 4712 - белое
