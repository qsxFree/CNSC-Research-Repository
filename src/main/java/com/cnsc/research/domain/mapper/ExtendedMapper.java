package com.cnsc.research.domain.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ExtendedMapper<D, T, E> extends GeneralMapper<D, T> {
    public abstract E toExtendedTransaction(D domainData);

    public List<E> toExtendedTransaction(Collection<D> domainData) {
        return domainData.stream().map(this::toExtendedTransaction)
                .collect(Collectors.toList());
    }

}
