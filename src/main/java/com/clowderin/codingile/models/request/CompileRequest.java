package com.clowderin.codingile.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompileRequest {
    private String languageCode;
    private String programBody;
}
