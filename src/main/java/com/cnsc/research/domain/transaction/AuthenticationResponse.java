package com.cnsc.research.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AuthenticationResponse {
    @Getter
    private String jws;
}
