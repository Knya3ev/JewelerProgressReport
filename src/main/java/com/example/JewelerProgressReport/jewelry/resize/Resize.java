package com.example.JewelerProgressReport.jewelry.resize;


import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.jewelry.jewelry_resize.JewelryResize;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "resize")
public class Resize {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "resize_seq", sequenceName = "resize_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resize_seq")
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "ring_resizing")
    private String ringResizing;
    @Column(name = "before")
    private Double before;
    @Column(name = "after")
    private Double after;

    @OneToMany(mappedBy = "resize", cascade = CascadeType.ALL)
    @Builder.Default
    private List<JewelryResize> jewelryResizes = new ArrayList<>();

    @OneToMany(mappedBy = "resize")
    @Builder.Default
    private List<Report> reports = new ArrayList<>();

    public void removeReport(Report report) {
        reports.remove(report);
        report.setResize(null);
    }
}
