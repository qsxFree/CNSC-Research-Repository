package com.cnsc.research.configuration.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Directory {

    private final String staticDirectory;

    public Directory(@Value("${static-directory}") String staticDirectory) {
        this.staticDirectory = staticDirectory;
    }

    public String getStaticDirectory() {
        return staticDirectory;
    }
}
