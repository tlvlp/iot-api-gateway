package com.tlvlp.iot.server.api.gateway.rpc;

import com.tlvlp.iot.server.api.gateway.services.BackendHooksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class BakcendHooksAPI {

    private BackendHooksService backendHooksService;

    public BakcendHooksAPI(BackendHooksService backendHooksService) {
        this.backendHooksService = backendHooksService;
    }

    @PostMapping("${API_GATEWAY_API_INCOMING_MQTT_MESSAGE}")
    public ResponseEntity handleIncomingMQTTMessage(@RequestBody Object message) {
        backendHooksService.handleIncomingMQTTMessage(message);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostMapping("${API_GATEWAY_API_OUTGOING_MQTT_MESSAGE}")
    public ResponseEntity handleOutgoingMQTTMessage(@RequestBody Object message) {
        backendHooksService.handleOutgoingMQTTMessage(message);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
