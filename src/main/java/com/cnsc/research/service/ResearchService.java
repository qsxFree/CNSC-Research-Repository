package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.ResearchMapper;
import com.cnsc.research.domain.model.Research;
import com.cnsc.research.domain.model.ResearchFile;
import com.cnsc.research.domain.model.ResearchStatus;
import com.cnsc.research.domain.repository.ResearchFileRepository;
import com.cnsc.research.domain.repository.ResearchRepository;
import com.cnsc.research.domain.transaction.ResearchBatchQueryResponse;
import com.cnsc.research.domain.transaction.ResearchBatchSaveResponse;
import com.cnsc.research.domain.transaction.ResearchDto;
import com.cnsc.research.domain.transaction.ResearchQueryBuilder;
import com.cnsc.research.misc.EntityBuilders;
import com.cnsc.research.misc.fields.ResearchFields;
import com.cnsc.research.misc.importer.CsvImport;
import com.cnsc.research.misc.storage.DigitalOceanStorageUtility;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

@Service
public class ResearchService {
    private final Logger logger;
    private final ResearchFileRepository researchFileRepository;
    private final ResearchRepository researchRepository;
    private final ResearchMapper researchMapper;
    private final EntityBuilders entityBuilder;
    private final DigitalOceanStorageUtility storageUtility;

    private MultipartFile csvFile;
    private CsvImport csv;


    @Autowired
    public ResearchService(Logger logger,
                           ResearchFileRepository researchFileRepository,
                           ResearchRepository researchRepository,
                           ResearchMapper researchMapper,
                           EntityBuilders entityBuilder,
                           DigitalOceanStorageUtility storageUtility
    ) {
        this.entityBuilder = entityBuilder;
        this.logger = logger;
        this.researchFileRepository = researchFileRepository;
        this.researchRepository = researchRepository;
        this.researchMapper = researchMapper;
        this.storageUtility = storageUtility;
    }

    public List<ResearchBatchSaveResponse> saveResearches(List<ResearchDto> researchDtos) {
        List<ResearchBatchSaveResponse> researchBatchSaveResponses = new ArrayList<>();
        researchDtos.forEach(researchDto -> researchBatchSaveResponses.add(saveResearch(researchDto)));
        return researchBatchSaveResponses;
    }

    public ResearchBatchSaveResponse saveResearch(ResearchDto researchDto) {
        logger.info(format("Research Title : %s", researchDto.getResearchFile().getTitle()));
        if (researchRepository.findResearchByTitleAndAvailability(researchDto.getResearchFile().getTitle())) {
            return new ResearchBatchSaveResponse(researchDto.getResearchFile().getTitle(), "Already Exist!");
        }

        Research research = researchMapper.toDomain(researchDto);
        logger.info(research.toString());
        researchRepository.save(validateRelationships(research));

        return new ResearchBatchSaveResponse(research.getResearchFile().getTitle(), "Saved!");
    }

    private Research validateRelationships(Research research) {
        research.setResearchFile(entityBuilder.buildResearchFile(research.getResearchFile().getTitle(), research.getResearchFile().getFileName()));

        research.setFundingAgencies(research.getFundingAgencies().stream()
                .map(fundingAgency -> entityBuilder.buildFundingAgency(fundingAgency.getAgencyName()))
                .collect(Collectors.toList()));

        research.setResearchers(research.getResearchers().stream()
                .map(researchers -> entityBuilder.buildResearcher(researchers.getName()))
                .collect(Collectors.toList()));

        research.setDeliveryUnit(entityBuilder.buildDeliveryUnit(research.getDeliveryUnit().getUnitName()));

        return research;
    }


    public List<ResearchDto> getResearchesFromCsv(MultipartFile file) throws Exception {
        this.csvFile = file;
        csv = new CsvImport<ResearchDto>(file.getBytes(), new ResearchFields());
        return csv.getMappedData(researchMapper);
    }


    public ResponseEntity processPdf(MultipartFile pdfFile) {
        String fileName = "" + System.currentTimeMillis();

        try {
            storageUtility.inContainer(DigitalOceanStorageUtility.PDF_CONTAINER)
                    .upload(pdfFile, fileName + ".pdf");
            return new ResponseEntity<String>(fileName, CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on writing file : " + pdfFile.getName(), INTERNAL_SERVER_ERROR);
        }
    }

    public ResearchBatchQueryResponse getAllResearches(int page, int size, String sortBy) {
        sortBy = sortBy.equals("title") ? "researchFile.title" : sortBy;

        Pageable pageRequest = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<Research> pageResult = researchRepository.findByDeletedFalse(pageRequest);

        int totalPage = pageResult.getTotalPages();
        long totalElements = pageResult.getTotalElements();
        int next = page + 1;
        int prev = page - 1;

        List<Research> queryResult = pageResult.getContent();
        ResearchBatchQueryResponse response = new ResearchBatchQueryResponse(researchMapper.toTransaction(queryResult), next, prev, totalPage, totalElements);
        return response;
    }

    public ResponseEntity updateResearch(ResearchDto researchDto) {
        try {
            if (!researchRepository.researchTitleExistNotMatchingID(researchDto.getResearchFile().getTitle(), researchDto.getId())) {
                Research research = researchMapper.toDomain(researchDto);
                researchRepository.save(research);
                return new ResponseEntity("Research has successfully updated", OK);
            } else {
                return new ResponseEntity("Research already exist", BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Error on updating research " + researchDto.getId(), INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity deleteResearch(Integer researchId) {
        try {
            Research research = researchRepository.findById(researchId).get();
            research.setDeleted(true);
            research.setDatetimeDeleted(LocalDateTime.now());
            researchRepository.save(research);
            return new ResponseEntity("Research has successfully deleted", OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Error on deleting research " + researchId, INTERNAL_SERVER_ERROR);
        }
    }

    public String deletePdf(String title) {
        File file = null;
        String fileName = file.getName().replace(".pdf", "");
        Optional<ResearchFile> researchFile = researchFileRepository.findByResearchTitleAndAvailabiity(title);
        if (file.exists()) file.delete();
        else return "There is nothing to delete.";
        if (researchFile.isPresent()) {
            researchFile.get().setFileName(null);
            researchFileRepository.save(researchFile.get());
            logger.info(format("%s.pdf has been removed", fileName));
        } else return format("%s is not linked to any records");
        return format("%s file has been removed", fileName);
    }

    public ResponseEntity getResearch(Integer researchId) {
        try {
            Optional<Research> research = researchRepository.findById(researchId);
            if (research.isPresent())
                return new ResponseEntity<ResearchDto>(researchMapper.toTransaction(research.get()), OK);
            else return new ResponseEntity<String>(format("Can't find research"), NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on retrieving research " + researchId, INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity deleteResearches(List<Integer> ids) {
        AtomicInteger deletedCount = new AtomicInteger(0);
        ids.forEach((researchId) -> {
            try {
                Research research = researchRepository.findById(researchId).get();
                research.setDeleted(true);
                research.getResearchFile().setDeleted(true);
                research.setDatetimeDeleted(LocalDateTime.now());
                researchRepository.save(research);
                deletedCount.getAndIncrement();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return new ResponseEntity(format("%d items has been deleted", deletedCount.get()), OK);
    }

    public List<ResearchDto> getResearches() {
        List<Research> researches = researchRepository.findByDeletedIsFalse();
        return researchMapper.toTransaction(researches);
    }

    public Double getMaxBudget() {
        return researchRepository.getMaxBudget();
    }

    public List<ResearchDto> getResearchByTitle(String title) {
        return researchMapper.toTransaction(researchRepository.findByResearchFile_TitleIsContainingIgnoreCaseAndDeletedIsFalse(title));
    }

    public List<ResearchDto> getResearchByAdvancedFilter(ResearchQueryBuilder queryBuilder) {
        Double budgetStart = null, budgetEnd = null;
        LocalDate startDate = null, endDate = null;
        List<ResearchStatus> status = null;
        if (queryBuilder.getBudget() != null) {
            budgetStart = queryBuilder.getBudget().get(0);
            budgetEnd = queryBuilder.getBudget().get(1);
        }
        //logger.info(format("Start %s ---- End %s",budgetStart.toString(),budgetEnd.toString() ));
        if (queryBuilder.getDate() != null) {
            startDate = queryBuilder.getDate().get(0);
            endDate = queryBuilder.getDate().get(1);
        }

        if (queryBuilder.getStatus() != null) {
            status = queryBuilder.getStatus().stream().map(item -> {
                ResearchStatus stat = ResearchStatus.NEW;
                switch (item.toLowerCase()) {
                    case "new":
                        stat = ResearchStatus.NEW;
                        break;
                    case "approved":
                        stat = ResearchStatus.APPROVED;
                        break;
                    case "completed":
                        stat = ResearchStatus.COMPLETED;
                        break;
                }
                return stat;
            }).collect(Collectors.toList());
        }
        List<Research> result = researchRepository.findAdvanced(
                budgetStart, budgetEnd,
                startDate, endDate,
                queryBuilder.getAgency(),
                queryBuilder.getUnit(),
                queryBuilder.getResearchers(),
                queryBuilder.getAgenda(),
                status
        );
        return researchMapper.toTransaction(result);
    }

    public ResponseEntity getPublicResearch() {
        try {
            return new ResponseEntity<List<ResearchDto>>(researchMapper
                    .toTransaction(researchRepository.findByDeletedIsFalse()), OK);

        } catch (Exception e) {
            return new ResponseEntity("Error on retrieving Researches", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity triggerVisibility(Long id) {
        try {
            Optional<Research> researchOptional = researchRepository.findById(Math.toIntExact(id));
            if (researchOptional.isPresent()) {
                Research research = researchOptional.get();
                research.setPublic(!research.isPublic());
                researchRepository.save(research);
                return new ResponseEntity<String>(research.isPublic() ? "The research is now public" : "The research is now private", OK);
            } else {
                return new ResponseEntity<String>("Research didn't exist", BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on updating research", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getResearchByStatus(String status) {
        try {
            ResearchStatus researchStatus = null;
            switch (status.toLowerCase()) {
                case "new":
                    researchStatus = ResearchStatus.NEW;
                    break;
                case "approved":
                    researchStatus = ResearchStatus.APPROVED;
                    break;
                case "completed":
                    researchStatus = ResearchStatus.COMPLETED;
                    break;
                default:
                    return new ResponseEntity<String>("Can't find researches", NOT_FOUND);
            }
            List<Research> researchList = researchRepository.findByResearchStatusAndDeletedIsFalseOrderByResearchFile_TitleAsc(researchStatus);
            return new ResponseEntity<List<ResearchDto>>(researchMapper.toTransaction(researchList), OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on retrieving researches", INTERNAL_SERVER_ERROR);
        }
    }
}
