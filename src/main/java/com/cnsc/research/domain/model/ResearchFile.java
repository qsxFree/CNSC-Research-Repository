package com.cnsc.research.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "research_file", schema = "research_repository")
@Data
public class ResearchFile {

    @Id
    @Column(name = "file_id", nullable = false)
    private Integer fileId;

    @Column(name = "title", nullable = false, length = 250)
    private String title;

    @Column(name = "file_name", nullable = true, length = 250)
    private String fileName;

    @Transient
    private Byte deleted;

}
