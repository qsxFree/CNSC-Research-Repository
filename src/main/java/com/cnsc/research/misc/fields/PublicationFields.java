package com.cnsc.research.misc.fields;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PublicationFields extends ValidFields {
    public static final String TITLE_KEY = "title";
    public static final String RESEARCHER_KEY = "researchers";
    public static final String LINK_KEY = "link";
    private static final Map<String, List<String>> PUBLICATION = Map.of(
            "title", Arrays.asList("title", "research title", "research_title", "publication title", "publication_title"),
            "researchers", Arrays.asList("researchers", "researcher", "proponents", "research team", "research_team", "author", "author/s", "researcher/s"),
            "link", Arrays.asList("url", "uri", "link", "address", "url_address", "url address", "web address", "link address", "uri address")

    );

    @Override
    protected Map<String, List<String>> getMap() {
        return PUBLICATION;
    }
}
