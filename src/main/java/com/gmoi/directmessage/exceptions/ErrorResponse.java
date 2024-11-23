package com.gmoi.directmessage.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private int error;
    private String message;
    private Object details;
}