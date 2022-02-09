package com.cnsc.research.service;

import com.cnsc.research.configuration.util.CurrentUser;
import com.cnsc.research.domain.mapper.PresentationMapper;
import com.cnsc.research.domain.model.Presentation;
import com.cnsc.research.domain.model.PresentationType;
import com.cnsc.research.domain.repository.PresentationRepository;
import com.cnsc.research.domain.repository.ResearchRepository;
import com.cnsc.research.domain.transaction.PresentationDto;
import com.cnsc.research.domain.transaction.PresentationQueryBuilder;
import com.cnsc.research.misc.fields.PresentationFields;
import com.cnsc.research.misc.importer.CsvImport;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.cnsc.research.domain.model.EntityType.PRESENTATION;
import static com.cnsc.research.domain.model.LogAction.*;
import static com.cnsc.research.domain.model.PresentationType.*;
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

@Service
public class PresentationService {

    private final PresentationRepository repository;
    private final PresentationMapper mapper;
    private final ResearchRepository researchRepository;
    private final Logger logger;
    private final LogService logService;

    @Autowired
    public PresentationService(PresentationRepository repository, PresentationMapper mapper, ResearchRepository researchRepository, Logger logger, LogService logService) {
        this.repository = repository;
        this.mapper = mapper;
        this.researchRepository = researchRepository;
        this.logger = logger;
        this.logService = logService;
    }

    public ResponseEntity getPresentation(Long presentationId) {
        try {
            Optional<Presentation> result = repository.findByPresentationIdAndDeletedIsFalse(presentationId);
            if (result.isPresent()) {
                return new ResponseEntity<PresentationDto>(mapper.toTransaction(result.get()), OK);
            } else {
                return new ResponseEntity<String>("Cannot find presentation", BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("Error on retrieving presentation", INTERNAL_SERVER_ERROR);
        }

    }

    //the toDomain mappings will do a database operation.
    //It will search for the existence of the research based on title
    public ResponseEntity addPresentation(PresentationDto presentationDto) {
        try {
            if (researchRepository.existsByResearchFile_TitleIgnoreCaseAndDeletedIsFalse(presentationDto.getPresentationTitle())) {
                Presentation savedData = repository.save(mapper.toDomain(presentationDto));
                logService.saveLog(savedData.getPresentationId(), CurrentUser.get().getId(), ADD, PRESENTATION);
                return new ResponseEntity<String>("Presentation has successfully added", OK);
            } else {
                return new ResponseEntity<String>("Invalid research title.", BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(format("Error on adding presentation."), INTERNAL_SERVER_ERROR);
        }
    }

    public String deletePresentation(Long presentationId) {
        String deleteMessage;
        Optional<Presentation> presentationOptional = repository.findById(presentationId);
        if (presentationOptional.isPresent()) {
            try {
                Presentation presentation = presentationOptional.get();
                if (!presentation.isDeleted()) {
                    presentation.setDeleted(true);
                    presentation.setDateTimeDeleted(LocalDateTime.now());
                    repository.save(presentation);
                    logService.saveLog(presentationId, CurrentUser.get().getId(), ARCHIVE, PRESENTATION);
                }
                deleteMessage = "Presentation has been deleted";
            } catch (Exception e) {
                deleteMessage = e.getMessage();
            }
        } else deleteMessage = "Presentation ID:" + presentationId + " didn't exist";

        return deleteMessage;
    }

    public List<PresentationDto> getPresentations() {
        return mapper.toTransaction(repository.findByDeletedIs(false));
    }

    public ResponseEntity editPresentation(PresentationDto presentationDto) {
        try {
            if (researchRepository.existsByResearchFile_TitleIgnoreCaseAndDeletedIsFalse(presentationDto.getPresentationTitle())) {
                repository.save(mapper.toDomain(presentationDto));
                logService.saveLog(presentationDto.getPresentationId(), CurrentUser.get().getId(), EDIT, PRESENTATION);
                return new ResponseEntity<String>("Presentation has successfully edited", OK);
            } else {
                return new ResponseEntity<String>("Invalid research title.", BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(format("Error on editing presentation."), INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity deletePresentations(List<Long> idList) {
        AtomicInteger deleteCount = new AtomicInteger(0);
        idList.forEach((id) -> {
            try {
                Optional<Presentation> presentationOptional = repository.findById(id);
                if (presentationOptional.isPresent()) {
                    Presentation presentation = presentationOptional.get();
                    presentation.setDeleted(true);
                    presentation.setDateTimeDeleted(LocalDateTime.now());
                    repository.save(presentation);
                    logService.saveLog(id, CurrentUser.get().getId(), ARCHIVE, PRESENTATION);
                    deleteCount.getAndIncrement();
                } else {
                    logger.error("Cannot delete presentation " + id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error on deleting presentation " + id);
            }
        });
        return new ResponseEntity(format("%d items has been deleted", deleteCount.get()), OK);
    }

    public ResponseEntity savePresentations(List<PresentationDto> presentationDtos) {
        List<String> resultList = presentationDtos.stream()
                .map(item -> {
                    String title = item.getPresentationTitle();
                    try {
                        Presentation presentation = repository.save(mapper.toDomain(item));
                        logService.saveLog(presentation.getPresentationId(), CurrentUser.get().getId(), ADD, PRESENTATION);
                        return format("Presentation Added ::: %s", title);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        return format("Can't add presentation ::: %s", title);
                    }
                })
                .collect(Collectors.toList());

        return new ResponseEntity(resultList, OK);
    }

    public List<PresentationDto> getPresentationFromFile(MultipartFile incomingFile) throws Exception {
        return new CsvImport<PresentationDto>(incomingFile.getBytes(), new PresentationFields())
                .getMappedData(mapper);
    }

    public List<PresentationDto> getPresentationByTitle(String title) {
        return mapper.toTransaction(repository.findByResearch_ResearchFile_TitleIsLikeIgnoreCaseAndDeletedIsFalse(title));
    }

    public List<PresentationDto> getPresentationByAdvancedFilter(PresentationQueryBuilder queryBuilder) {
        LocalDate startDate = null, endDate = null;
        List<PresentationType> types = null;
        if (queryBuilder.getDate() != null) {
            startDate = queryBuilder.getDate().get(0);
            endDate = queryBuilder.getDate().get(1);
        }

        if (queryBuilder.getType() != null) {
            types = queryBuilder.getType().stream().map(item -> {
                PresentationType type = null;
                switch (item.toLowerCase()) {
                    case "local":
                        type = LOCAL;
                        break;
                    case "regional":
                        type = REGIONAL;
                        break;
                    case "national":
                        type = NATIONAL;
                        break;
                    case "international":
                        type = INTERNATIONAL;
                        break;
                    default:
                }
                return type;
            }).collect(Collectors.toList());
        }

        List<Presentation> result = repository.findAdvanced(
                queryBuilder.getResearchers(),
                startDate,
                endDate,
                types
        );
        return mapper.toTransaction(result);
    }

    public ResponseEntity triggerVisibility(Long id) {
        try {
            Optional<Presentation> presentationOptional = repository.findById(id);
            if (presentationOptional.isPresent()) {
                Presentation presentation = presentationOptional.get();
                presentation.setPublic(!presentation.isPublic());
                repository.save(presentation);
                logService.saveLog(id, CurrentUser.get().getId(), EDIT, PRESENTATION);
                return new ResponseEntity<String>(presentation.isPublic() ? "The presentation is now public" : "The research is now private", OK);
            } else {
                return new ResponseEntity<String>("Presentation didn't exist", BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on updating presentation", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getPublicPresentations() {
        try {
            List<PresentationDto> presentationDtos = mapper.toTransaction(repository.findByDeletedIs(false));
            return new ResponseEntity<List<PresentationDto>>(presentationDtos, OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on retrieving presentations", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getPresentationByType(String presentationType) {
        try {
            PresentationType type = null;
            switch (presentationType.toLowerCase()) {
                case "local":
                    type = LOCAL;
                    break;
                case "regional":
                    type = REGIONAL;
                    break;
                case "national":
                    type = NATIONAL;
                    break;
                case "international":
                    type = INTERNATIONAL;
                    break;
                default:
                    return new ResponseEntity<String>("Can't retrieve presentation", NOT_FOUND);
            }
            List<Presentation> presentationList = repository.findByTypeAndDeletedFalseOrderByResearch_ResearchFile_TitleAsc(type);
            return new ResponseEntity<List<PresentationDto>>(mapper.toTransaction(presentationList), OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error on retrieving presentations", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getPresentationByResearcher(Integer id) {
        try {
            List<Presentation> presentationList = repository.findByResearch_Researchers_ResearcherIdAndDeletedIsFalseOrderByResearch_ResearchFile_FileNameAsc(id);
            return new ResponseEntity<List<PresentationDto>>(mapper.toTransaction(presentationList), OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error on retrieving presentations", INTERNAL_SERVER_ERROR);
        }
    }


}
