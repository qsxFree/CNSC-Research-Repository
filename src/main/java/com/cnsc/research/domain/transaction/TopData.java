package com.cnsc.research.domain.transaction;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TopData {
    private List<ResearchDto> recentlyAdded;
    private List<ResearchDto> mostViewed;
}
