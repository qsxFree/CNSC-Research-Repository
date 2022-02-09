package com.cnsc.research.service;

import com.cnsc.research.domain.model.DataLog;
import com.cnsc.research.domain.model.EntityType;
import com.cnsc.research.domain.model.LogAction;
import com.cnsc.research.domain.repository.DataLogRepository;
import com.cnsc.research.domain.transaction.LogDto;
import com.cnsc.research.domain.transaction.TrackingInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
public class LogService {
    private final DataLogRepository logRepository;

    @Autowired
    LogService(DataLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    void saveLog(long presentationId, long userId, LogAction action, EntityType type) {
        DataLog logData = DataLog.builder()
                .dataId(presentationId)
                .userId(userId)
                .entityType(type.name())
                .action(action.name())
                .build();
        logRepository.save(logData);
    }

    public ResponseEntity getLogs(EntityType type) {
        try {
            List<LogDto> logs;
            switch (type) {
                case RESEARCH:
                    logs = logRepository.getAllResearchLog();
                    break;
                case PRESENTATION:
                    logs = logRepository.getAllPresentationLog();
                    break;
                case PUBLICATION:
                    logs = logRepository.getAllPublicationLog();
                    break;
                default:
                    return new ResponseEntity("Invalid Entity type", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(logs, OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error on retrieving " + type.name().toLowerCase() + " logs", INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity getLog(EntityType type, long dataId) {
        try {
            TrackingInformation metadata = new TrackingInformation();

            Optional<LogDto> optionalLogDto;
            List<LogDto> logDtoList;

            switch (type) {
                case RESEARCH:
                    optionalLogDto = logRepository.addResearchMetadata(dataId);
                    logDtoList = logRepository.editResearchMetadata(dataId);
                    break;
                case PRESENTATION:
                    optionalLogDto = logRepository.addPresentationMetadata(dataId);
                    logDtoList = logRepository.editPresentationMetadata(dataId);
                    break;
                case PUBLICATION:
                    optionalLogDto = logRepository.addPublicationMetadata(dataId);
                    logDtoList = logRepository.editPublicationMetadata(dataId);
                    break;
                default:
                    return new ResponseEntity("Invalid Entity type", HttpStatus.BAD_REQUEST);
            }


            if (optionalLogDto.isPresent()) {
                metadata.setAddedBy(optionalLogDto.get().getName());
                metadata.setDateTimeAdded(optionalLogDto.get().getDateTime());
            }
            if (!logDtoList.isEmpty()) {
                metadata.setEditedBy(logDtoList.get(0).getName());
                metadata.setDateTimeEdited(logDtoList.get(0).getDateTime());
            }

            return new ResponseEntity<>(metadata, OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error on retrieving metadata", INTERNAL_SERVER_ERROR);
        }
    }

}
