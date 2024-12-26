package com.esp.govbrsignatureintegration.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    private Date timestamp;
    private String message;
    private String description;
    private int status;

    public ErrorMessage(Date timestamp, String message, String description) {
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }

}
