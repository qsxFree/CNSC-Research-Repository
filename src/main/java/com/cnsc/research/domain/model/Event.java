package com.cnsc.research.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_datetime")
    private LocalDateTime eventDatetime;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "is_private")
    private boolean isPrivate;

    @Column(name = "datetime_created")
    private LocalDateTime datetimeCreated;

    @Column(name = "datetime_deleted")
    private LocalDateTime datetimeDeleted;

    @Column(name = "deleted")
    private Boolean deleted;
}
