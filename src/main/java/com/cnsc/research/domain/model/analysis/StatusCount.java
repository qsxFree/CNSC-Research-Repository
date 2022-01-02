package com.cnsc.research.domain.model.analysis;

import com.cnsc.research.domain.model.ResearchStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
public class StatusCount {
    private String status;
    private Long count;

    public StatusCount(ResearchStatus status, Long count) {
        this.status = StringUtils.capitalize(status.name());
        this.count = count;
    }
}
