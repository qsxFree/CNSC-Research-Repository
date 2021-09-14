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

    public ExtendedPublicationDto(Long publicationId,String publicationTitle,String publicationLink,List<ResearchersDto> researchers){
        super(publicationId,publicationTitle,publicationTitle);
        this.researchers = researchers;
    }
}
