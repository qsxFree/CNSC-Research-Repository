package com.cnsc.research.misc.fields;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PresentationFields extends ValidFields {

    public static final String TITLE_KEY = "title";
    public static final String RESEARCHER_KEY = "researchers";
    public static final String TYPE_KEY = "type";
    public static final String DATE_KEY = "date";

    private static final Map<String, List<String>> PRESENTATION = Map.of(
            TITLE_KEY, Arrays.asList("title", "research title", "research_title", "research"),
            TYPE_KEY, Arrays.asList("type", "level", "scope", "location", "region", "address", "presentation type", "presentation_type", "presentation scope", "presentation_scope", "presentation region", "presentation_region", "presentation level", "presentation_level"),
            DATE_KEY, Arrays.asList("date", "date_presented", "date presented", "presentation date", "presentation_date")
    );

    @Override
    protected Map<String, List<String>> getMap() {
        return PRESENTATION;
    }
}
