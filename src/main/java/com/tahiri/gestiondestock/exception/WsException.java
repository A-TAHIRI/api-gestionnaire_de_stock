package com.tahiri.gestiondestock.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class WsException extends ResponseStatusException {
    public WsException(HttpStatusCode httpStatusCode, String message) {
        super(httpStatusCode, message);
    }


}