package com.cnsc.research.domain.transaction;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveDto<T> {
    private T data;
    private boolean retrievable;
}
