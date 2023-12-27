package com.example.JewelerProgressReport.users.user;


import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.shop.Shop;
import com.example.JewelerProgressReport.users.enums.roles.Role;
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
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User implements UserDetails {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "users_seq", sequenceName = "users_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "roles")
    private Set<Role> roles = new HashSet<>();

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "telegram_id")
    private long telegramId;

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

    @Column(name = "in_staff")
    private boolean inStaff = false;

    @Column(name = "pending_approval")
    private boolean pendingApproval = false;

    public void addReport(Report report) {
        reports.add(report);
        report.setUser(this);
    }

    public void removeReport(Report report) {
        reports.remove(report);
        report.setUser(null);
        List<String> roles ;

        roles = this.roles.stream().map(role -> role.getCode()).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

