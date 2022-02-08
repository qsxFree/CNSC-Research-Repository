package com.cnsc.research.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogDto {
    private Long logId;
    private String username;
    private String name;
    private LocalDateTime dateTime;
    private String action;
    private Long dataId;
}
