package com.cnsc.research.domain.model.analysis;

import com.cnsc.research.domain.model.PresentationType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PresentationTypeCount {
    private String type;
    private Long count;

    public PresentationTypeCount(PresentationType type, Long count) {
        this.type = type.name();
        this.count = count;
    }
}
