package com.example.JewelerProgressReport.entity;


import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String numberPhone;

    private int countVisits = 0;

    private LocalDateTime lastVisit;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Report> reports = new ArrayList<>();

    @ManyToMany(mappedBy = "clients", fetch = FetchType.LAZY)
    private List<Jewelry> jewelries = new ArrayList<>();

    public Client(String numberPhone, int countVisits, LocalDateTime lastVisit) {
        this.numberPhone = numberPhone;
        this.countVisits += 1;
        this.lastVisit = lastVisit;
    }

    public void addReports(Report report){
        reports.add(report);
        report.setClient(this);
    }
    public void removeReport(Report report){
        reports.remove(report);
        report.setClient(null);
    }
    public void addJewelry(Jewelry jewelry){
        jewelries.add(jewelry);
        jewelry.getClients().add(this);
    }
    public void removeJewelry(Jewelry jewelry){
        jewelries.remove(jewelry);
        jewelry.getClients().remove(this);
    }
}
