package com.cnsc.research.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtendedPublicationDto extends PublicationDto {
    private List<ResearchersDto> researchers;
}
