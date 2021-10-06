package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Publication;
import com.cnsc.research.domain.transaction.ExtendedPublicationDto;
import com.cnsc.research.domain.transaction.PublicationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublicationMapper extends ExtendedMapper<Publication, PublicationDto, ExtendedPublicationDto> {

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


}
