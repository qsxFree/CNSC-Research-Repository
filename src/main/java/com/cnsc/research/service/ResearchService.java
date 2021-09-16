package com.cnsc.research.service;

import com.cnsc.research.domain.exception.InvalidCsvFieldException;
import com.cnsc.research.domain.exception.InvalidFileFormat;
import com.cnsc.research.domain.mapper.ResearchMapper;
import com.cnsc.research.domain.model.*;
import com.cnsc.research.domain.repository.*;
import com.cnsc.research.domain.transaction.ResearchBatchQueryResponse;
import com.cnsc.research.domain.transaction.ResearchBatchSaveResponse;
import com.cnsc.research.domain.transaction.ResearchDto;
import com.cnsc.research.misc.CsvHandler;
import com.cnsc.research.misc.EntityBuilders;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
public class ResearchService {
    private final Logger logger;
    private final ResearchFileRepository researchFileRepository;
    private final ResearchRepository researchRepository;
    private final ResearchMapper researchMapper;
    private final EntityBuilders entityBuilder;

    @Value("${static-directory}")
    private String staticDirectory;

    private MultipartFile csvFile;
    private CsvHandler csv;
    private UserDetails currentUser;


    @Autowired
    public ResearchService(Logger logger,
                           ResearchFileRepository researchFileRepository,
                           ResearchRepository researchRepository,
                           ResearchMapper researchMapper,
                           EntityBuilders entityBuilder
    ) {
        this.entityBuilder = entityBuilder;
        this.logger = logger;
        this.researchFileRepository = researchFileRepository;
        this.researchRepository = researchRepository;
        this.researchMapper = researchMapper;
    }

    public List<ResearchBatchSaveResponse> saveResearches(List<ResearchDto> researchDtos) {
        List<ResearchBatchSaveResponse> researchBatchSaveResponses = new ArrayList<>();
        researchDtos.forEach(researchDto -> researchBatchSaveResponses.add(saveResearch(researchDto)));
        return researchBatchSaveResponses;
    }

    public ResearchBatchSaveResponse saveResearch(ResearchDto researchDto) {
        if (researchRepository.findResearchByTitle(researchDto.getResearchFile().getTitle()).isPresent()) {
            return new ResearchBatchSaveResponse(researchDto.getResearchFile().getTitle(), "Already Exist!");
        }

        Research research = researchMapper.toResearch(researchDto);
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


    public List<ResearchDto> getResearchesFromCsv(MultipartFile file) throws IOException, InvalidCsvFieldException, CsvException {
        this.csvFile = file;
        csv = rewriteFileToLocal();
        return researchMapper.csvToResearchDto(csv.getRows(), csv.getRowIndices());
    }

    private CsvHandler rewriteFileToLocal() throws IOException {
        File file = new File(staticDirectory + csvFile.getOriginalFilename());//creating directory

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        logger.info("writing file...");

        fileOutputStream.write(csvFile.getBytes());//rewriting temporary file
        CsvHandler handler = new CsvHandler(file);
        file.delete();
        return handler;
    }

    private File getPdfFile(String fileName) {
        String newName = fileName.replaceAll(" ", "-");
        newName = newName.replaceAll("[./\\:?*\"|]", "");
        return new File(staticDirectory + "pdf/" + newName + ".pdf");
    }

    //TODO find a solution for pdf caching to avoid duplicates on upload
    public String processPdf(Integer id, String title, MultipartFile pdfFile) throws FileNotFoundException, InvalidFileFormat, FileAlreadyExistsException {
        //currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        File file = getPdfFile(title);
        if (!pdfFile.getOriginalFilename().endsWith(".pdf"))
            throw new InvalidFileFormat(format("%s is not a pdf file"));

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            logger.info("Saving PDF file ...");
            fileOutputStream.write(pdfFile.getBytes());

        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            logger.info(format("Error in writing pdf file to static directory"));
            e.printStackTrace();
        }
        logger.info("File saved!");
        return file.getName().replace(".pdf", "");
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
        ResearchBatchQueryResponse response = new ResearchBatchQueryResponse(researchMapper.toResearchDto(queryResult), next, prev, totalPage, totalElements);
        return response;
    }

    public ResponseEntity updateResearch(ResearchDto researchDto) {
        ResponseEntity<String> response = null;

        try {
            Research research = researchMapper.toResearch(researchDto);
            researchRepository.save(research);
            response = new ResponseEntity("Research Successfully Updated!", OK);
        } catch (Exception e) {
            response = new ResponseEntity("Update Error!", INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    public ResponseEntity deleteResearch(Integer researchId) {
        ResponseEntity<String> response = null;
        try {
            Research research = researchRepository.findById(researchId).get();
            research.setDeleted(true);
            research.setDatetimeDeleted(LocalDateTime.now());
            researchRepository.save(research);
            response = new ResponseEntity("Research Successfully Deleted!", OK);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ResponseEntity("Delete Error!", INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public String deletePdf(String title) {
        File file = getPdfFile(title);
        String fileName = file.getName().replace(".pdf", "");
        Optional<ResearchFile> researchFile = researchFileRepository.findByTitleIgnoreCase(title);
        if (file.exists()) file.delete();
        else return "There is nothing to delete.";
        if (researchFile.isPresent()) {
            researchFile.get().setFileName(null);
            researchFileRepository.save(researchFile.get());
            logger.info(format("%s.pdf has been removed", fileName));
        } else return format("%s is not linked to any records");
        return format("%s file has been removed", fileName);
    }

    public ResearchDto getResearch(Integer researchId) throws Exception {
        Optional<Research> research = researchRepository.findById(researchId);
        if (research.isPresent()) return researchMapper.toResearchDto(research.get());
        else throw new Exception("The research didn't exist");
    }

    public ResponseEntity deleteResearches(List<Integer> ids) {
        AtomicInteger deletedCount = new AtomicInteger(0);
        ids.forEach((researchId) -> {
            try {
                Research research = researchRepository.findById(researchId).get();
                research.setDeleted(true);
                research.setDatetimeDeleted(LocalDateTime.now());
                researchRepository.save(research);
                deletedCount.getAndIncrement();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return new ResponseEntity(format("%d items has been deleted", deletedCount.get()), OK);
    }
}
