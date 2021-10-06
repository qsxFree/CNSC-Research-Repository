package com.cnsc.research.misc.importer;

public class InvalidFieldException extends Exception {
    public InvalidFieldException(String s) {
        super(String.format("Cannot find valid field named `%s` ", s));
    }
}
