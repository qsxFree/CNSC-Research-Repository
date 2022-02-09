package com.cnsc.research.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_log")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class DataLog {
    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(name = "data_id")
    private Long dataId;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "action")
    private String action;

    @Column(name = "datetime_commit", insertable = false)
    private LocalDateTime dateTimeCommit;

    @Column(name = "user_id")
    private Long userId;
}
