package com.cnsc.research.domain.exception;

public class AccountNotFound extends Throwable {
    public AccountNotFound(int id) {
        super(String.format("Cannot find account by id %d",id));
    }
}
