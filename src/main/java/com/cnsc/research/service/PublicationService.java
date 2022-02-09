package com.cnsc.research.service;

import com.cnsc.research.configuration.util.CurrentUser;
import com.cnsc.research.domain.mapper.PublicationMapper;
import com.cnsc.research.domain.model.Publication;
import com.cnsc.research.domain.model.Researchers;
import com.cnsc.research.domain.repository.PublicationRepository;
import com.cnsc.research.domain.transaction.ExtendedPublicationDto;
import com.cnsc.research.domain.transaction.PublicationQueryBuilder;
import com.cnsc.research.misc.EntityBuilders;
import com.cnsc.research.misc.fields.PublicationFields;
import com.cnsc.research.misc.importer.CsvImport;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.cnsc.research.domain.model.EntityType.PUBLICATION;
import static com.cnsc.research.domain.model.LogAction.*;
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
public class PublicationService {
    private final PublicationRepository repository;
    private final Logger logger;
    private final PublicationMapper publicationMapper;
    private final EntityBuilders entityBuilders;
    private final LogService logService;

    @Autowired
    public PublicationService(PublicationRepository repository,
                              PublicationMapper publicationMapper,
                              Logger logger,
                              EntityBuilders entityBuilders,
                              LogService logService) {
        this.repository = repository;
        this.logger = logger;
        this.publicationMapper = publicationMapper;
        this.entityBuilders = entityBuilders;
        this.logService = logService;
    }

    public ResponseEntity addPublication(ExtendedPublicationDto publicationDto) {
        try {
            Publication publication = publicationMapper.toDomain(publicationDto);
            //REMINDER -- this might cause a performance issue someday.
            // do some profiling and change the implementation.
            List<Researchers> researchers = publicationDto.getResearchers()
                    .stream()
                    .map(item -> entityBuilders.buildResearcher(item.getResearcherName()))
                    .collect(Collectors.toList());

            publication.setResearchers(researchers);
            Publication savedData = repository.save(publication);
            logService.saveLog(savedData.getPublicationId(), CurrentUser.get().getId(), ADD, PUBLICATION);
            return new ResponseEntity<String>("Publication has successfully added", OK);
        } catch (Exception e) {
            logger.error(format("Error on saving \"%s\"", publicationDto.getPublicationTitle()));
            logger.error(e.getMessage());
            return new ResponseEntity("Error on saving publication", INTERNAL_SERVER_ERROR);
        }
    }

    public ExtendedPublicationDto getPublication(Long publicationId) {
        return publicationMapper.toExtendedTransaction(repository.getById(publicationId));
    }

    public ResponseEntity editPublication(ExtendedPublicationDto publicationDto) {
        try {
            repository.save(publicationMapper.toExtendedDomain(publicationDto));
            logService.saveLog(publicationDto.getPublicationId(), CurrentUser.get().getId(), EDIT, PUBLICATION);
            return new ResponseEntity<String>("Publication has successfully edited", OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Error on editing presentation", INTERNAL_SERVER_ERROR);
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
                logService.saveLog(id, CurrentUser.get().getId(), ARCHIVE, PUBLICATION);
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
            return publicationMapper.toExtendedTransaction(publicationList);
        } catch (Exception e) {
            logger.error(format("Line 98 : %s", e.getMessage()));
            return List.of();
        }
    }

    public ResponseEntity savePublications(List<ExtendedPublicationDto> publicationDtos) {
        List<String> resultList = publicationDtos
                .stream()
                .map(item -> {
                    String title = item.getPublicationTitle();
                    try {
                        repository.save(publicationMapper.toDomain(item));
                        logService.saveLog(item.getPublicationId(), CurrentUser.get().getId(), ADD, PUBLICATION);
                        return format("Publication Added ::: %s", title);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        return format("Can't add presentation ::: %s", title);
                    }
                })
                .collect(Collectors.toList());

        return new ResponseEntity(resultList, OK);
    }

    public ResponseEntity deletePublications(List<Long> idList) {
        AtomicInteger deleteCount = new AtomicInteger(0);
        idList.forEach((id) -> {
            //FIXME This is not safe validation. This might change someday
            if (this.deletePublication(id).equals("Publication has been deleted"))
                deleteCount.getAndIncrement();
        });
        return new ResponseEntity(format("%d items has been deleted", deleteCount.get()), OK);
    }

    public List<ExtendedPublicationDto> getPublicationFromFile(MultipartFile incomingFile) throws Exception {
        return new CsvImport<ExtendedPublicationDto>(incomingFile.getBytes(), new PublicationFields())
                .getMappedData(publicationMapper);
    }

    public List<ExtendedPublicationDto> getPublicationByTitle(String title) {
        return publicationMapper.toExtendedTransaction(repository.findByPublicationTitleIsContainingIgnoreCaseAndDeletedIsFalse(title));
    }

    public List<ExtendedPublicationDto> getPublicationByAdvancedFilter(PublicationQueryBuilder queryBuilder) {
        return publicationMapper.toExtendedTransaction(repository.findAdvanced(queryBuilder.getResearchers()));
    }

    public ResponseEntity getPublicPublication() {
        try {
            List<Publication> publicationList = repository.findByDeletedIsFalse();
            return new ResponseEntity<List<ExtendedPublicationDto>>(publicationMapper.toExtendedTransaction(publicationList), OK);
        } catch (Exception e) {
            logger.error(format("Line 98 : %s", e.getMessage()));
            return new ResponseEntity("Error on retrieving publications", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getPublicationByResearcher(Integer id) {
        try {
            return new ResponseEntity(publicationMapper.toExtendedTransaction(repository.findByResearchers_ResearcherIdIsAndDeletedIsFalse(id)), OK);
        } catch (Exception e) {
            return new ResponseEntity("Error on retrieving publication", INTERNAL_SERVER_ERROR);
        }
    }
}
