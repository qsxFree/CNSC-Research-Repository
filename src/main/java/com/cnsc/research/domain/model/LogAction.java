package com.cnsc.research.domain.model;

public enum LogAction {
    ADD("ADD"),
    EDIT("EDIT"),
    DELETE("DELETE");

    private String action;

    private LogAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
