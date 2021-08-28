package com.cnsc.research.domain.exception;

public class InvalidFileFormat extends Throwable {
    public InvalidFileFormat(String format) {
        super(format);
    }
}
