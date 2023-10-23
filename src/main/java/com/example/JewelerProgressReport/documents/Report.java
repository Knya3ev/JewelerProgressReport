package com.example.JewelerProgressReport.documents;


import com.example.JewelerProgressReport.jewelry.enums.JewelleryProduct;
import com.example.JewelerProgressReport.jewelry.enums.JewelleryOperation;
import com.example.JewelerProgressReport.jewelry.enums.Metal;
import com.example.JewelerProgressReport.users.client.Client;
import com.example.JewelerProgressReport.users.user.User;
import com.example.JewelerProgressReport.jewelry.resize.Resize;
import lombok.*;

import jakarta.persistence.*;

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
    private int count; //  количество изделий

    @Column(name = "jewellery_product")
    private JewelleryProduct jewelleryProduct; // тип изделия

    @Column(name = "metal")
    private Metal metal; // цвет изделия(металла)

    @Column(name = "jewellery_operations")
    private List<JewelleryOperation> jewelleryOperations; // тип операции

    @Column(name = "details_of_operation")
    private String detailsOfOperation; // подробности операции

    @Column(name = "union_code_jewelry")
    private String unionCodeJewelry; // уникальный код изделия

    @Column(name = "article")
    private String article; //  артикул изделия

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


    public void removePersonAndClientAndResizes() {
        user.removeReport(this);
        client.removeReport(this);

        if(this.resize != null) {
            resize.removeReport(this);
        }
    }

}

