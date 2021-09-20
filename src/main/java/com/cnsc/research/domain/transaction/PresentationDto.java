package com.cnsc.research.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PresentationDto {
    private Long presentationId;
    private String presentationTitle;
    private String presentationType;
    private List<ResearchersDto> researchers;
    private LocalDate presentationDate;


}
