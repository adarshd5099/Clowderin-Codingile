package com.clowderin.Codingile.models.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CompileRequest {
    private String languageCode;
    private String programBody;
    private String input;
}
