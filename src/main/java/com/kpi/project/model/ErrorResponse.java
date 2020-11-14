package com.kpi.project.model;

import com.kpi.project.model.enums.ErrorTypes;
import lombok.Data;

@Data
public class ErrorResponse {

    ErrorTypes errorType;
    String message;
}
