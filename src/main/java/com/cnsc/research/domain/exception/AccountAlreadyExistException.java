package com.cnsc.research.domain.exception;

import com.cnsc.research.domain.model.User;

public class AccountAlreadyExistException extends Throwable {
    public AccountAlreadyExistException(String username){
        super(String.format("%s already exist",username));
    }

    public AccountAlreadyExistException(User user) {
        this(user.getUsername());
    }
}
