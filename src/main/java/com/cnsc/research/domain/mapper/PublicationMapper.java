package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Publication;
import com.cnsc.research.domain.transaction.ExtendedPublicationDto;
import com.cnsc.research.domain.transaction.PublicationDto;
import com.cnsc.research.domain.transaction.ResearchersDto;
import com.cnsc.research.misc.fields.PublicationFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PublicationMapper extends ExtendedMapper<Publication, PublicationDto, ExtendedPublicationDto> implements DataImportMapper<Publication, ExtendedPublicationDto> {

    private final ResearcherMapper researcherMapper;

    @Autowired
    public PublicationMapper(ResearcherMapper researcherMapper) {
        this.researcherMapper = researcherMapper;
    }

    public PublicationDto toTransaction(Publication publication) {
        return new PublicationDto(publication.getPublicationId(),
                publication.getPublicationTitle(),
                publication.getPublicationLink());
    }

    public ExtendedPublicationDto toExtendedTransaction(Publication publication) {
        ExtendedPublicationDto extendedPublicationDto = new ExtendedPublicationDto(publication.getPublicationId(),
                publication.getPublicationTitle(),
                publication.getPublicationLink(),
                researcherMapper.toTransaction(publication.getResearchers())
        );
        return extendedPublicationDto;
    }

    public Publication toDomain(PublicationDto publicationDto) throws Exception {
        return Publication.builder()
                .publicationId(publicationDto.getPublicationId())
                .publicationTitle(publicationDto.getPublicationTitle())
                .publicationLink(publicationDto.getPublicationLink())
                .build();
    }


    @Override
    public ExtendedPublicationDto dataImportToTransaction(String[] cellData, Map<String, Integer> keyArrangement) {
        List<ResearchersDto> researchers = Arrays
                .stream(cellData[keyArrangement.get(PublicationFields.RESEARCHER_KEY)]
                        .split(","))
                .map(item -> new ResearchersDto(null, item))
                .collect(Collectors.toList());

        return new ExtendedPublicationDto(null,
                cellData[keyArrangement.get(PublicationFields.TITLE_KEY)],
                cellData[keyArrangement.get(PublicationFields.LINK_KEY)],
                researchers
        );
    }

    @Override
    public List<ExtendedPublicationDto> dataImportToTransaction(List<String[]> rowData, Map<String, Integer> keyArrangement) {
        return rowData.stream()
                .map(csvRow -> this.dataImportToTransaction(csvRow, keyArrangement))
                .collect(Collectors.toList());
    }
}
