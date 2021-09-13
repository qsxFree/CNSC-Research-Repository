package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Publication;
import com.cnsc.research.domain.transaction.ExtendedPublicationDto;
import com.cnsc.research.domain.transaction.PublicationDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PublicationMapper {
    public static PublicationDto toPublicationDto(Publication publication) {
        return new PublicationDto(publication.getPublicationId(),
                publication.getPublicationTitle(),
                publication.getPublicationLink());
    }
    public static List<PublicationDto> toPublicationDto(Collection<Publication> publications){
        return publications.stream()
                .map(PublicationMapper::toPublicationDto)
                .collect(Collectors.toList());
    }

    public static ExtendedPublicationDto toExtendedPublicationDto(Publication publication){
        ExtendedPublicationDto extendedPublicationDto = (ExtendedPublicationDto) toPublicationDto(publication);
        extendedPublicationDto.setResearchers(ResearcherMapper.toResearchDto(publication.getResearchers()));
        return extendedPublicationDto;
    }

    public static List<ExtendedPublicationDto> toExtededPublicationDto(Collection<Publication> publications){
        return publications.stream().map(PublicationMapper::toExtendedPublicationDto)
                .collect(Collectors.toList());
    }


    public static Publication toPublication(ExtendedPublicationDto publicationDto){
        return Publication.builder()
                .publicationId(publicationDto.getPublicationId())
                .publicationTitle(publicationDto.getPublicationTitle())
                .publicationLink(publicationDto.getPublicationLink())
                .build();
    }

    public static List<Publication> toPublication(Collection<ExtendedPublicationDto> extendedPublicationDtos){
        return extendedPublicationDtos.stream()
                .map(PublicationMapper::toPublication)
                .collect(Collectors.toList());
    }

}
