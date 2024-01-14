package org.eclipse.jakarta;

import lombok.Builder;

import javax.json.bind.annotation.JsonbProperty;

@Builder
public class SuccessResponse {
    private String message;
    private int statusCode;
}
