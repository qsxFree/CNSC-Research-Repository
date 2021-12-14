package com.cnsc.research.service;

import com.cnsc.research.domain.report.ResearchReport;
import com.cnsc.research.domain.repository.ResearchRepository;
import com.cnsc.research.domain.transaction.ResearchDto;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
public class ReportService {


    private final ResearchRepository repository;

    @Autowired
    public ReportService(ResearchRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity generateResearchReport(List<ResearchDto> researchDtoList) {
        try {
            List<ResearchReport> researchList = researchDtoList.stream()
                    .map(data -> {
                        ResearchReport researchReport = new ResearchReport();
                        researchReport.setTitle(data.getResearchFile().getTitle());
                        researchReport.setAgenda(data.getResearchAgenda()
                                .stream()
                                .map(ResearchDto.ResearchAgenda::getAgendaName).collect(Collectors.joining(",\n")));

                        researchReport.setResearcher(data.getResearchers()
                                .stream()
                                .map(ResearchDto.Researchers::getName).collect(Collectors.joining(",\n")));

                        researchReport.setDeliveryUnit(data.getDeliveryUnit().getUnitName());

                        researchReport.setFundingAgency(data.getFundingAgency()
                                .stream()
                                .map(ResearchDto.FundingAgency::getAgencyName).collect(Collectors.joining(",\n")));

                        researchReport.setBudget(data.getBudget());
                        researchReport.setStatus(data.getResearchStatus());
                        researchReport.setRemarks(data.getRemarks());

                        return researchReport;
                    }).collect(Collectors.toList());

            File file = ResourceUtils.getFile("classpath:research-report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(researchList);
            Map<String, Object> parameters = new HashedMap();
            parameters.put("createdBy", "ruuuyin");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            String path = String.format("D:\\Documents\\Research - %s.pdf", new Long(System.currentTimeMillis()).toString());
            JasperExportManager.exportReportToPdfFile(jasperPrint, path);
            return new ResponseEntity("Report generated : " + path, OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Error on generating report", INTERNAL_SERVER_ERROR);
        }
    }
}
