package com.tlvlp.iot.server.api.gateway.rpc;

import com.tlvlp.iot.server.api.gateway.services.InternalHooksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class InternalHooksAPI {

    private InternalHooksService internalHooksService;

    public InternalHooksAPI(InternalHooksService internalHooksService) {
        this.internalHooksService = internalHooksService;
    }

    @PostMapping("${API_GATEWAY_API_INCOMING_MQTT_MESSAGE}")
    public ResponseEntity handleIncomingMQTTMessage(@RequestBody Object message) {
        internalHooksService.handleIncomingMQTTMessage(message);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
