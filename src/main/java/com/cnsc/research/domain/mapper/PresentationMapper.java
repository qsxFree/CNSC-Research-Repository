package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Presentation;
import com.cnsc.research.domain.model.PresentationType;
import com.cnsc.research.domain.transaction.PresentationDto;
import com.cnsc.research.misc.EntityBuilders;
import com.cnsc.research.misc.fields.PresentationFields;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cnsc.research.domain.model.PresentationType.*;

@Component
public class PresentationMapper extends GeneralMapper<Presentation, PresentationDto> implements DataImportMapper<Presentation, PresentationDto> {

    private final EntityBuilders entityBuilders;
    private final ResearcherMapper researcherMapper;
    private final Logger logger;
    private final DateTimeFormatter formatter;

    @Autowired
    public PresentationMapper(EntityBuilders entityBuilders, ResearcherMapper researcherMapper, Logger logger, DateTimeFormatter formatter) {
        this.entityBuilders = entityBuilders;
        this.researcherMapper = researcherMapper;
        this.logger = logger;
        this.formatter = formatter;
    }

    public PresentationDto toTransaction(Presentation presentation) {
        return PresentationDto.builder()
                .presentationId(presentation.getPresentationId())
                .organizingAgency(presentation.getOrganizingAgency())
                .eventName(presentation.getEventName())
                .presentationDate(presentation.getPresentationDate())
                .presentationType(presentation.getType().name().toLowerCase())
                .presentationTitle(presentation.getResearch().getResearchFile().getTitle())
                .researchers(researcherMapper.toTransaction(presentation.getResearch().getResearchers()))
                .build();
    }


    public Presentation toDomain(PresentationDto presentationDto) throws Exception {
        PresentationType type = null;
        switch (presentationDto.getPresentationType().toLowerCase()) {
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
        return Presentation.builder()
                .presentationId(presentationDto.getPresentationId())
                .type(type)
                .organizingAgency(presentationDto.getOrganizingAgency())
                .eventName(presentationDto.getEventName())
                .presentationDate(presentationDto.getPresentationDate())
                .research(entityBuilders.buildResearchFromDb(presentationDto.getPresentationTitle()))
                .build();
    }

    @Override
    public PresentationDto dataImportToTransaction(String[] cellData, Map<String, Integer> keyArrangement) {
        return new PresentationDto(null,
                cellData[keyArrangement.get(PresentationFields.TITLE_KEY)],
                cellData[keyArrangement.get(PresentationFields.TYPE_KEY)],
                null,
                LocalDate.from(formatter.parse(cellData[keyArrangement.get(PresentationFields.DATE_KEY)].trim())),
                "",//TODO: change the organizing agency from import mapping
                true,//TODO: change the privacy based on import
                ""//TODO: change the event name based on event domain
        );
    }

    @Override
    public List<PresentationDto> dataImportToTransaction(List<String[]> rowData, Map<String, Integer> keyArrangement) {
        return rowData.stream()
                .map(csvRow -> this.dataImportToTransaction(csvRow, keyArrangement))
                .collect(Collectors.toList());
    }
}
