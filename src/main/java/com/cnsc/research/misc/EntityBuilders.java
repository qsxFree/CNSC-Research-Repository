package com.cnsc.research.misc;

import com.cnsc.research.domain.model.*;
import com.cnsc.research.domain.repository.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handles all the creation of instance of models
 * from the database and checks if the value exist.
 *
 * @author ruyin
 */
@Component
public class EntityBuilders {
    private final FundingAgencyRepository fundingAgencyRepository;
    private final ResearchersRepository researchersRepository;
    private final DeliveryUnitRepository deliveryUnitRepository;
    private final ResearchFileRepository researchFileRepository;
    private final ResearchRepository researchRepository;
    private final ResearchAgendaRepository researchAgendaRepository;
    private final Logger logger;

    @Autowired
    public EntityBuilders(FundingAgencyRepository fundingAgencyRepository,
                          ResearchersRepository researchersRepository,
                          DeliveryUnitRepository deliveryUnitRepository,
                          ResearchFileRepository researchFileRepository,
                          ResearchRepository researchRepository, ResearchAgendaRepository researchAgendaRepository, Logger logger) {
        this.fundingAgencyRepository = fundingAgencyRepository;
        this.researchersRepository = researchersRepository;
        this.deliveryUnitRepository = deliveryUnitRepository;
        this.researchFileRepository = researchFileRepository;
        this.researchRepository = researchRepository;
        this.researchAgendaRepository = researchAgendaRepository;
        this.logger = logger;
    }

    /**
     * Creates a Delivery Unit instance if the unit name
     * didn't exist in the database.
     *
     * @param unitName - name of the delivery unit
     * @return DeliveryUnit
     */
    public DeliveryUnit buildDeliveryUnit(String unitName) {
        Optional<DeliveryUnit> deliveryUnit = deliveryUnitRepository.findByUnitNameIgnoreCase(unitName);
        return deliveryUnit.orElse(DeliveryUnit
                .builder()
                .unitName(unitName)
                .build());
    }

    /**
     * Creates a Funding Agency instance if the agency name
     * didn't exist in the database.
     *
     * @param agencyName - name of the funding agency
     * @return FundingAgency
     */
    public FundingAgency buildFundingAgency(String agencyName) {
        Optional<FundingAgency> fundingAgency = fundingAgencyRepository.findByAgencyNameIgnoreCase(agencyName);
        return fundingAgency.orElse(FundingAgency
                .builder()
                .agencyName(agencyName)
                .build()
        );
    }

    /**
     * Creates a Researcher instance if the researcher name
     * didn't exist in the database.
     *
     * @param name - researcher's name
     * @return Researchers
     */
    public Researchers buildResearcher(String name) {
        Optional<Researchers> researchers = researchersRepository.findByNameIgnoreCaseAndNotDeleted(name);
        return researchers.orElse(Researchers
                .builder()
                .name(name)
                .build()
        );
    }

    /**
     * Creates a Research File instance if the research title
     * didn't exist in the database.
     *
     * @param title    - title of the research
     * @param fileName - name of the file
     * @return ResearchFile
     */
    public ResearchFile buildResearchFile(String title, String fileName) {
        Optional<ResearchFile> researchFile = researchFileRepository.findByResearchTitleAndAvailabiity(title);
        return researchFile.orElse(ResearchFile
                .builder()
                .title(title)
                .fileName(fileName)
                .build());
    }

    /**
     * Creates a research instance from the database.
     * This is different from other entity builders because it
     * only returns the data that exist in the database
     *
     * @param title - title of the the research.
     * @return Research
     * @throws Exception - throws an error if the data didn't exist.or the data is flagged deleted
     */
    public Research buildResearchFromDb(String title) throws Exception {
        Optional<Research> research = researchRepository.findResearchByTitle(title);
        return research.orElseThrow(() -> new Exception(String.format("cannot find %s ", title)));
    }

    public ResearchAgenda buildResearchAgenda(String agenda) {
        Optional<ResearchAgenda> researchAgenda = researchAgendaRepository.findByAgendaIgnoreCase(agenda);
        return researchAgenda.orElse(ResearchAgenda
                .builder()
                .agenda(agenda)
                .build()
        );
    }

}
