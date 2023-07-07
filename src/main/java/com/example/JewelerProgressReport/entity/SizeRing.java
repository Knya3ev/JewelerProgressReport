package com.example.JewelerProgressReport.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "size_ring")
public class SizeRing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "ring_resizing")
    private String ringResizing;
    @Column(name = "before")
    private Double before;
    @Column(name = "after")
    private Double after;

    @ManyToMany(mappedBy = "sizeRings", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Jewelry> jewelry = new ArrayList<>();

    @OneToMany(mappedBy = "resizes")
    @Builder.Default
    private List<Report> report = new ArrayList<>();

}
