package com.cnsc.research.domain.mapper;

import java.util.Collection;
import java.util.List;

public interface ListMapper<D, T> {
     List<D> toDomain(Collection<T> transactionsData);

     List<T> toTransaction(Collection<D> domainData);
}
