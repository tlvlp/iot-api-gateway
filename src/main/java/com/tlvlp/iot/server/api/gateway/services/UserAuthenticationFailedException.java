package com.tlvlp.iot.server.api.gateway.services;

public class UserAuthenticationFailedException extends Exception {
    public UserAuthenticationFailedException(String error) {
        super(error);
    }
}
