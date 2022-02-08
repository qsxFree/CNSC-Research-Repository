package com.cnsc.research.domain.transaction;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TrackingInformation {
    private String addedBy;
    private String editedBy;
    private LocalDateTime dateTimeAdded;
    private LocalDateTime dateTimeEdited;
}
