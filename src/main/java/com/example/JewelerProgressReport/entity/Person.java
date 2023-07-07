package com.example.JewelerProgressReport.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "person")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "address")
    private String address;
    @Column(name = "telegram_id")
    private String telegramId;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Column(name = "is_verification")
    private boolean isVerification = false;
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<Report> reports = new ArrayList<>();

    public void addReport(Report report) {
        reports.add(report);
        report.setPerson(this);
    }

    public void removeReport(Report report) {
        reports.remove(report);
        report.setPerson(null);
    }

}

