package com.gauravd70.commons;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class GenericResponse {
    private String message;
}
