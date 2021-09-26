package com.cnsc.research.misc.fields;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ResearchFields extends ValidFields {

    private static final Map<String, List<String>> RESEARCH = Map.of(
            "title", Arrays.asList("title", "research title", "research_title"),
            "researchers", Arrays.asList("researchers", "researcher", "proponents", "research team", "research_team"),
            "deliveryUnit", Arrays.asList("delivery_unit", "delivery unit", "delivery_team", "delivery team", "department"),
            "fundingAgency", Arrays.asList("funding_agency", "funding agency", "sponsor", "sponsors"),
            "budget", Arrays.asList("budget", "amount", "approved_budget"),
            "startDate", Arrays.asList("start_date", "start date", "date_started", "date started"),
            "endDate", Arrays.asList("end_date", "end date", "date_ended", "date end"),
            "researchStatus", Arrays.asList("status", "research_status", "research status"),
            "remark", Arrays.asList("remark", "remarks", "with moa", "with_moa", "comment", "comments")
    );

    @Override
    protected Map<String, List<String>> getMap() {
        return RESEARCH;
    }

}
