package com.tlvlp.iot.server.api.gateway.rpc;

import com.tlvlp.iot.server.api.gateway.services.GatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayAPI {

    //TODO
    // get all units by id (UnitS)
    // get unit by id (UnitS)
    // request global status (UnitS)
    // unit module control (UnitS)
    // get scheduled tasks for a unit (SchedulerS)
    // add scheduled task to unit (SchedulerS) (UnitS)
    // delete task from unit (SchedulerS) (UnitS)
    // get reports for unit (ReportingS)

    private GatewayService gatewayService;

    public GatewayAPI(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("${API_GATEWAY_API_GET_ALL_UNITS}")
    public ResponseEntity getAllUnits() {
        return gatewayService.getAllUnits();
    }


}
