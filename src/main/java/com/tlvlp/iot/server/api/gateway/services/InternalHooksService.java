package com.tlvlp.iot.server.api.gateway.services;

import com.tlvlp.iot.server.api.gateway.config.Properties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class InternalHooksService {

    private Properties properties;
    private RestTemplate restTemplate;

    public InternalHooksService(Properties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    public void handleIncomingMQTTMessage(Map<String, Object> message) {
        HashMap response = forwardMessageToUnitService(message);
        String messageType = (String) response.get("type");
        switch (messageType) {
            case "error":
                // Notify subscribers
                break;
            case "inactive":
                // Notify subscribers
                break;
            case "status":
                forwardUnitModuleUpdatesToReporting((UnitModules) response.get("object"));
                // Notify subscribers
                break;
        }

    }

    private HashMap forwardMessageToUnitService(Map<String, Object> message) {
        return restTemplate.postForObject(
                String.format("http://%s:%s%s",
                        properties.getUNIT_SERVICE_NAME(),
                        properties.getUNIT_SERVICE_PORT(),
                        properties.getUNIT_SERVICE_API_INCOMING_MESSAGE()),
                message,
                HashMap.class
        );
    }

    private HashMap forwardUnitModuleUpdatesToReporting(UnitModules unitModules) {
        return restTemplate.postForObject(
                String.format("http://%s:%s%s",
                        properties.getREPORTING_SERVICE_NAME(),
                        properties.getREPORTING_SERVICE_PORT(),
                        properties.getREPORTING_SERVICE_API_POST_VALUES()),
                unitModules,
                HashMap.class
        );
    }
}
