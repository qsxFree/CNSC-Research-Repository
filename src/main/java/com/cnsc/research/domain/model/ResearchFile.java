package com.cnsc.research.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "research_file")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResearchFile {

    @Id
    @Column(name = "file_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileId;

    @Column(name = "title", nullable = false, length = 250)
    private String title;

    @Column(name = "file_name", nullable = true, length = 250)
    private String fileName;

    @OneToOne(mappedBy = "researchFile", fetch = FetchType.LAZY)
    private Research research;

    @Transient
    private Byte deleted;

}
