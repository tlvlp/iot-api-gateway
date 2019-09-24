package com.tlvlp.iot.server.api.gateway.services;

import com.tlvlp.iot.server.api.gateway.config.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GatewayService {

    private static final Logger log = LoggerFactory.getLogger(GatewayService.class);
    private RestTemplate restTemplate;
    private Properties properties;

    public GatewayService(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public ResponseEntity<List> getAllUnits() {
        return restTemplate.getForEntity(
                String.format("http://%s:%s%s",
                        properties.getUNIT_SERVICE_NAME(),
                        properties.getUNIT_SERVICE_PORT(),
                        properties.getUNIT_SERVICE_API_LIST_ALL_UNIT()),
                List.class);
    }

    public ResponseEntity getUnitById(String unitID) {
        return restTemplate.getForEntity(
                String.format("http://%s:%s%s?unitID=%s",
                        properties.getUNIT_SERVICE_NAME(),
                        properties.getUNIT_SERVICE_PORT(),
                        properties.getUNIT_SERVICE_API_GET_UNIT_BY_ID(),
                        unitID),
                String.class);
    }
}
