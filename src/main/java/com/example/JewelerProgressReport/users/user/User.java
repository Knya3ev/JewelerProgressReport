package com.example.JewelerProgressReport.users.user;


import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.shop.Shop;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "users_seq", sequenceName = "users_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "telegram_id")
    private String telegramId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_verification")
    private boolean isVerification = false; // возможно убрать, реализация будет через role

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "director", fetch = FetchType.LAZY)
    private List<Shop> shopOwnership = new ArrayList<>();

    @Column(name = "count_shops")
    private int countShops;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public void addReport(Report report) {
        reports.add(report);
        report.setUser(this);
    }

    public void removeReport(Report report) {
        reports.remove(report);
        report.setUser(null);
    }
}

