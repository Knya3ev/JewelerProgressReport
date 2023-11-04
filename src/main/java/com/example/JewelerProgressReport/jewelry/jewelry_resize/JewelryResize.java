package com.example.JewelerProgressReport.jewelry.jewelry_resize;


import com.example.JewelerProgressReport.jewelry.Jewelry;
import com.example.JewelerProgressReport.jewelry.resize.Resize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jewelry_resize")
public class JewelryResize {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "jewelry_resize_seq", sequenceName = "jewelry_resize_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jewelry_resize_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jewelry_id")
    private Jewelry jewelry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resize_id")
    private Resize resize;
}
