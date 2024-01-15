package org.eclipse.jakarta.repository;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResponseMessage {
    private String message;
    private Integer statusCode;
}
