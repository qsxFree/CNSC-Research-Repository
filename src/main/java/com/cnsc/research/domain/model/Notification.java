package com.cnsc.research.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notification_id;

    @Column(name = "notification_header")
    private String notificationHeader;

    @Column(name = "viewed")
    private boolean viewed;

    @Column(name = "notification_source")
    private String notificationSource;

    @Column(name = "referencced_ids")
    private String referencedIds;

    @Column(name = "datetime_added", insertable = false)
    private LocalDateTime dateTimeAdded;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "notification_user_rel",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<User> userList;

}
