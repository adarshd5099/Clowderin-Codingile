package com.clowderin.Codingile.models.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompileRequest {
    private String languageCode;
    private String programBody;
}
