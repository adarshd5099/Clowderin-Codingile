package com.clowderin.codingile.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompileResultDto {
    private String responseStatus;
    private String programOutput;
}
