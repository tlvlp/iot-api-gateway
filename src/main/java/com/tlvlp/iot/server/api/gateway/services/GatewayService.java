package com.tlvlp.iot.server.api.gateway.services;

import com.tlvlp.iot.server.api.gateway.config.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class GatewayService {

    private static final Logger log = LoggerFactory.getLogger(GatewayService.class);
    private RestTemplate restTemplate;
    private Properties properties;

    public GatewayService(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }




}
