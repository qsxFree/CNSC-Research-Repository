package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.PublicationMapper;
import com.cnsc.research.domain.model.Publication;
import com.cnsc.research.domain.model.Researchers;
import com.cnsc.research.domain.repository.PublicationRepository;
import com.cnsc.research.domain.transaction.ExtendedPublicationDto;
import com.cnsc.research.domain.transaction.PublicationSaveResponse;
import com.cnsc.research.domain.transaction.PublicationDto;
import com.cnsc.research.misc.EntityBuilders;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class PublicationService {
    private final PublicationRepository repository;
    private final Logger logger;
    private final PublicationMapper publicationMapper;
    private final EntityBuilders entityBuilders;

    @Autowired
    public PublicationService(PublicationRepository repository,
                              PublicationMapper publicationMapper,
                              Logger logger,
                              EntityBuilders entityBuilders
    ) {
        this.repository = repository;
        this.logger = logger;
        this.publicationMapper = publicationMapper;
        this.entityBuilders = entityBuilders;
    }

    public PublicationSaveResponse addPublication(ExtendedPublicationDto publicationDto) {
        if (repository.findByPublicationTitleIgnoreCaseAndDeleted(publicationDto.getPublicationTitle(), false).isPresent())
            return new PublicationSaveResponse(publicationDto.getPublicationTitle(), "Already Exist!");
        Publication publication = publicationMapper.toPublication(publicationDto);

        //REMINDER -- this might cause a performance issue someday.
        // run profiling and change the implementation.
        List<Researchers> researchers = publication.getResearchers()
                .stream()
                .map(item -> entityBuilders.buildResearcher(item.getName()))
                .collect(Collectors.toList());

        publication.setResearchers(researchers);

        try {
            repository.save(publication);
            return new PublicationSaveResponse(publication.getPublicationTitle(), "Saved!");
        } catch (Exception e) {
            logger.error(format("Error on saving %s", publication.getPublicationId()));
            logger.error(e.getMessage());
            return new PublicationSaveResponse(publication.getPublicationTitle(), "Error!");
        }
    }

    public PublicationDto getPublication(Long publicationId) {
        return publicationMapper.toPublicationDto(repository.getById(publicationId));
    }

    public String editPublication(PublicationDto publicationDto) {
        Publication publication = publicationMapper.toPublication((ExtendedPublicationDto) publicationDto);
        try {
            repository.save(publication);
            return "Publication saved";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String deletePublication(Long id) {
        String deleteMessage;
        Optional<Publication> publicationOptional = repository.findById(id);
        if (publicationOptional.isPresent()) {
            try {
                Publication publication = publicationOptional.get();
                publication.setDeleted(true);
                publication.setDateTimeDeleted(LocalDateTime.now());
                repository.save(publication);
                deleteMessage = "Publication has been deleted";
            } catch (Exception e) {
                deleteMessage = e.getMessage();
            }
        } else deleteMessage = "Publication ID:" + id + " didn't exist";

        return deleteMessage;
    }

    public List<ExtendedPublicationDto> getPublications() {
        try {
            List<Publication> publicationList = repository.findByDeletedIsFalse();
            return publicationMapper.toExtededPublicationDto(publicationList);
        } catch (Exception e) {
            logger.error(format("Line 98 : %s", e.getMessage()));
            return List.of();
        }
    }

    public List<PublicationSaveResponse> savePublications(List<ExtendedPublicationDto> publicationDtos) {
        return publicationDtos
                .stream()
                .map(this::addPublication)
                .collect(Collectors.toList());
    }

    public List<String> deletePublications(List<Long> idList) {
        return idList.stream()
                .map(this::deletePublication)
                .collect(Collectors.toList());
    }
}
