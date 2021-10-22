package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.PresentationMapper;
import com.cnsc.research.domain.model.Presentation;
import com.cnsc.research.domain.model.PresentationType;
import com.cnsc.research.domain.repository.PresentationRepository;
import com.cnsc.research.domain.transaction.PresentationDto;
import com.cnsc.research.domain.transaction.PresentationQueryBuilder;
import com.cnsc.research.domain.transaction.PresentationSaveResponse;
import com.cnsc.research.misc.EntityBuilders;
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

import static com.cnsc.research.domain.model.PresentationType.*;
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.OK;

@Service
public class PresentationService {

    private final PresentationRepository repository;
    private final PresentationMapper mapper;
    private final Logger logger;
    private final EntityBuilders builders;

    @Autowired
    public PresentationService(PresentationRepository repository, PresentationMapper mapper, Logger logger, EntityBuilders builders) {
        this.repository = repository;
        this.mapper = mapper;
        this.logger = logger;
        this.builders = builders;
    }

    public PresentationDto getPresentation(Long presentationId) {
        Optional<Presentation> result = repository.findByPresentationIdAndDeletedIsFalse(presentationId);
        return result.isPresent() ? mapper.toTransaction(result.get()) : null;
    }

    public PresentationSaveResponse addPresentation(PresentationDto presentationDto) throws Exception {
        //the toPresentation mappings will do a database operation.
        //It will search for the existence of the research based on title
        Presentation presentation = mapper.toDomain(presentationDto);
        String researchTitle = presentation.getResearch().getResearchFile().getTitle();
        PresentationType type = presentation.getType();
        if (repository.existByTitleAndType(researchTitle, type))
            return new PresentationSaveResponse(researchTitle, "Already Exist!");
        else {
            repository.save(presentation);
            return new PresentationSaveResponse(researchTitle, "Saved!");
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

    public String editPresentation(PresentationDto presentationDto) {
        try {
            Presentation presentation = mapper.toDomain(presentationDto);
            repository.save(presentation);
            return "Publication saved";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public ResponseEntity deletePresentations(List<Long> idList) {
        AtomicInteger deleteCount = new AtomicInteger(0);
        idList.forEach((id) -> {
            //FIXME This is not safe validation. This might change someday
            if (this.deletePresentation(id).equals("Presentation has been deleted"))
                deleteCount.getAndIncrement();
        });
        return new ResponseEntity(format("%d items has been deleted", deleteCount.get()), OK);
    }

    public List<PresentationSaveResponse> savePresentations(List<PresentationDto> presentationDtos) {
        return presentationDtos.stream()
                .map(item -> {
                    try {
                        return addPresentation(item);
                    } catch (Exception e) {
                        return new PresentationSaveResponse(item.getPresentationTitle(), "Didn't saved");
                    }
                })
                .collect(Collectors.toList());
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
}
