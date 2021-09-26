package com.cnsc.research.domain.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GeneralMapper<D, T> {

     /**
      * To domain list.
      *
      * @param transactionData the transaction data
      * @return the list
      */
     public List<D> toDomain(Collection<T> transactionData) {
          return transactionData.stream()
                  .map(this::toDomain)
                  .collect(Collectors.toList());
     }

     /**
      * To transaction list.
      *
      * @param domainData the domain data
      * @return the list
      */
     public List<T> toTransaction(Collection<D> domainData) {
          return domainData.stream()
                  .map(this::toTransaction)
                  .collect(Collectors.toList());
     }

     /**
      * To domain d.
      *
      * @param transactionsData the transactions data
      * @return the d
      */
     public abstract D toDomain(T transactionsData);

     /**
      * To transaction t.
      *
      * @param domainData the domain data
      * @return the t
      */
     public abstract T toTransaction(D domainData);
}
