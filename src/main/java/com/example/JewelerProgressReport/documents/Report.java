package com.example.JewelerProgressReport.documents;


import com.example.JewelerProgressReport.documents.enums.StatusReport;
import com.example.JewelerProgressReport.jewelry.enums.JewelleryOperation;
import com.example.JewelerProgressReport.jewelry.enums.JewelleryProduct;
import com.example.JewelerProgressReport.jewelry.enums.Metal;
import com.example.JewelerProgressReport.jewelry.resize.Resize;
import com.example.JewelerProgressReport.shop.Shop;
import com.example.JewelerProgressReport.users.client.Client;
import com.example.JewelerProgressReport.users.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "report", schema = "public")
public class Report {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "report_seq", sequenceName = "report_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_seq")
    @Column(name = "id")
    private Long id;


    @Column(name = "count")
    @Builder.Default
    private int count = 1; //  количество изделий

    @Column(name = "jewellery_product")
    private JewelleryProduct jewelleryProduct; // тип изделия

    @Column(name = "metal")
    private Metal metal; // цвет изделия(металла)

    @Column(name = "jewellery_operations")
    private List<JewelleryOperation> jewelleryOperations; // тип операции

    @Column(name = "details_of_operation")
    private String detailsOfOperation; // подробности операции

    @Column(name = "size_before")
    private Double sizeBefore;

    @Column(name = "size_after")
    private Double sizeAfter;

    @Column(name = "union_code_jewelry")
    private String unionCodeJewelry; // уникальный код изделия

    @Column(name = "article")
    private String article; //  артикул изделия

    @Column(name = "status")
    private StatusReport status;

    @Column(name = "created_date")
    private LocalDateTime createdDate; // дата оказания услуги

    @Builder.Default
    @Column(name = "is_edit")
    private boolean isEdit = false;

    @Column(name = "edit_date")
    private LocalDateTime editDate;


    @ManyToOne(fetch = FetchType.LAZY)
    private Resize resize;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    private Shop shop;


    public void removePersonAndClientAndResizes() {
        user.removeReport(this);
        client.removeReport(this);

        if(this.resize != null) {
            resize.removeReport(this);
        }
    }

}

