package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Publication;
import com.cnsc.research.domain.transaction.ExtendedPublicationDto;
import com.cnsc.research.domain.transaction.PublicationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PublicationMapper {

    private final ResearcherMapper researcherMapper;

    @Autowired
    public PublicationMapper(ResearcherMapper researcherMapper) {
        this.researcherMapper = researcherMapper;
    }

    public  PublicationDto toPublicationDto(Publication publication) {
        return new PublicationDto(publication.getPublicationId(),
                publication.getPublicationTitle(),
                publication.getPublicationLink());
    }
    public  List<PublicationDto> toPublicationDto(Collection<Publication> publications){
        return publications.stream()
                .map(this::toPublicationDto)
                .collect(Collectors.toList());
    }

    public  ExtendedPublicationDto toExtendedPublicationDto(Publication publication){
        ExtendedPublicationDto extendedPublicationDto = new ExtendedPublicationDto(publication.getPublicationId(),
                publication.getPublicationTitle(),
                publication.getPublicationLink(),
                researcherMapper.toResearchDto(publication.getResearchers())
        );
        return extendedPublicationDto;
    }

    public  List<ExtendedPublicationDto> toExtededPublicationDto(Collection<Publication> publications){
        return publications.stream().map(this::toExtendedPublicationDto)
                .collect(Collectors.toList());
    }

    public  Publication toPublication(ExtendedPublicationDto publicationDto){
        return Publication.builder()
                .publicationId(publicationDto.getPublicationId())
                .publicationTitle(publicationDto.getPublicationTitle())
                .publicationLink(publicationDto.getPublicationLink())
                .researchers(researcherMapper.toResearcher(publicationDto.getResearchers()))
                .build();
    }

    public  List<Publication> toPublication(Collection<ExtendedPublicationDto> extendedPublicationDtos){
        return extendedPublicationDtos.stream()
                .map(this::toPublication)
                .collect(Collectors.toList());
    }

}
