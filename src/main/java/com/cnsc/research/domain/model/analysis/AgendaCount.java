package com.cnsc.research.domain.model.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaCount implements Serializable {
    private String agenda;
    private Long count;
}
