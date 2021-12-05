package com.cnsc.research.domain.transaction;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {

    private Long eventId;
    private String eventName;
    private String eventType;
    private boolean isPrivate;
    private LocalDateTime eventDatetime;
}
