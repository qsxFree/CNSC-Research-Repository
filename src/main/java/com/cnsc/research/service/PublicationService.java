package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.PublicationMapper;
import com.cnsc.research.domain.model.Publication;
import com.cnsc.research.domain.model.Researchers;
import com.cnsc.research.domain.repository.PublicationRepository;
import com.cnsc.research.domain.transaction.ExtendedPublicationDto;
import com.cnsc.research.domain.transaction.PublicationDto;
import com.cnsc.research.domain.transaction.PublicationSaveResponse;
import com.cnsc.research.misc.EntityBuilders;
import com.cnsc.research.misc.UploadHandler;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.OK;

@Service
public class PublicationService {
    private final PublicationRepository repository;
    private final Logger logger;
    private final PublicationMapper publicationMapper;
    private final EntityBuilders entityBuilders;

    private UploadHandler uploadHandler;
    private UserDetails currentUser;

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

    public ExtendedPublicationDto getPublication(Long publicationId) {
        return publicationMapper.toExtendedPublicationDto(repository.getById(publicationId));
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

    public ResponseEntity deletePublications(List<Long> idList) {
        AtomicInteger deleteCount = new AtomicInteger(0);
        idList.forEach((id) -> {
            //FIXME This is not safe validation. This might change someday
            if (this.deletePublication(id).equals("Publication has been deleted"))
                deleteCount.getAndIncrement();
        });
        return new ResponseEntity(format("%d items has been deleted", deleteCount.get()), OK);
    }

    public List<PublicationDto> processXls(MultipartFile incomingFile) throws IOException {
        currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        uploadHandler = new UploadHandler(currentUser.getUsername());
        uploadHandler.process(incomingFile);
        uploadHandler.deleteCachedFile();
        return List.of(null);
    }
}
