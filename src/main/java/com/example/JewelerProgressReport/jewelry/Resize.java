package com.example.JewelerProgressReport.jewelry;


import com.example.JewelerProgressReport.documents.Report;
import com.example.JewelerProgressReport.jewelry.Jewelry;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(mappedBy = "resizes", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Jewelry> jewelry = new HashSet<>();

    @OneToMany(mappedBy = "resize")
    @Builder.Default
    private List<Report> reports = new ArrayList<>();

    public void removeReport(Report report) {
        reports.remove(report);
        report.setResize(null);
    }
}
