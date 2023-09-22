package com.example.JewelerProgressReport.documents;


import com.example.JewelerProgressReport.users.client.Client;
import com.example.JewelerProgressReport.users.person.Person;
import com.example.JewelerProgressReport.jewelry.Resize;
import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "report")
public class Report {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "report_seq", sequenceName = "report_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "count")
    private int count; //  количество изделий

    @Column(name = "type_product")
    private String typeProduct; // тип изделия

    @Column(name = "type_of_metal_color")
    private String typeOfMetalColor; // цвет изделия(металла)

    @Column(name = "type_of_operation")
    private String typeOfOperation; // тип операции

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
    private Person person;
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;


    public void removePersonAndClientAndResizes() {
        person.removeReport(this);
        client.removeReport(this);
        resize.removeReport(this);
    }

}

