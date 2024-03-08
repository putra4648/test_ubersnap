package com.putra.test_ubersnap.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ValidatorResponse {
    private final String required;
    private final String message;
}
