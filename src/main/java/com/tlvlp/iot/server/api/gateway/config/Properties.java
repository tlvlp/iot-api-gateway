package com.tlvlp.iot.server.api.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Properties {

    // The service uses environment variables from the Docker container.

    // MQTT Client

    @Value("${MQTT_CLIENT_SERVICE_NAME}")
    private String MQTT_CLIENT_SERVICE_NAME;

    @Value("${MQTT_CLIENT_SERVICE_PORT}")
    private String MQTT_CLIENT_SERVICE_PORT;

    @Value("${MQTT_CLIENT_API_OUTGOING_MESSAGE}")
    private String MQTT_CLIENT_API_OUTGOING_MESSAGE;

    // Unit service

    @Value("${UNIT_SERVICE_NAME}")
    private String UNIT_SERVICE_NAME;

    @Value("${UNIT_SERVICE_PORT}")
    private String UNIT_SERVICE_PORT;

    @Value("${UNIT_SERVICE_API_LIST_ALL_UNIT}")
    private String UNIT_SERVICE_API_LIST_ALL_UNIT;

    @Value("${UNIT_SERVICE_API_GET_UNIT_BY_ID}")
    private String UNIT_SERVICE_API_GET_UNIT_BY_ID;

    @Value("${UNIT_SERVICE_API_REQUEST_GLOBAL_STATUS}")
    private String UNIT_SERVICE_API_REQUEST_GLOBAL_STATUS;

    @Value("${UNIT_SERVICE_API_MODULE_CONTROL}")
    private String UNIT_SERVICE_API_MODULE_CONTROL;

    @Value("${UNIT_SERVICE_API_ADD_SCHEDULED_EVENT}")
    private String UNIT_SERVICE_API_ADD_SCHEDULED_EVENT;

    @Value("${UNIT_SERVICE_API_DELETE_SCHEDULED_EVENT}")
    private String UNIT_SERVICE_API_DELETE_SCHEDULED_EVENT;

    @Value("${UNIT_SERVICE_API_INCOMING_MESSAGE}")
    private String UNIT_SERVICE_API_INCOMING_MESSAGE;

    @Value("${UNIT_SERVICE_API_GET_UNIT_LOGS}")
    private String UNIT_SERVICE_API_GET_UNIT_LOGS;

    // Scheduler

    @Value("${SCHEDULER_SERVICE_NAME}")
    private String SCHEDULER_SERVICE_NAME;

    @Value("${SCHEDULER_SERVICE_PORT}")
    private String SCHEDULER_SERVICE_PORT;

    @Value("${SCHEDULER_SERVICE_API_GET_EVENTS_FROM_LIST}")
    private String SCHEDULER_SERVICE_API_GET_EVENTS_FROM_LIST;

    @Value("${SCHEDULER_SERVICE_API_POST_EVENT}")
    private String SCHEDULER_SERVICE_API_POST_EVENT;

    @Value("${SCHEDULER_SERVICE_API_DELETE_EVENT}")
    private String SCHEDULER_SERVICE_API_DELETE_EVENT;


    // Reporting

    @Value("${REPORTING_SERVICE_NAME}")
    private String REPORTING_SERVICE_NAME;

    @Value("${REPORTING_SERVICE_PORT}")
    private String REPORTING_SERVICE_PORT;

    @Value("${REPORTING_SERVICE_API_GET_AVERAGES}")
    private String REPORTING_SERVICE_API_GET_AVERAGES;

    @Value("${REPORTING_SERVICE_API_POST_VALUES}")
    private String REPORTING_SERVICE_API_POST_VALUES;


    // Getters

    public String getMQTT_CLIENT_SERVICE_NAME() {
        return MQTT_CLIENT_SERVICE_NAME;
    }

    public String getMQTT_CLIENT_SERVICE_PORT() {
        return MQTT_CLIENT_SERVICE_PORT;
    }

    public String getMQTT_CLIENT_API_OUTGOING_MESSAGE() {
        return MQTT_CLIENT_API_OUTGOING_MESSAGE;
    }

    public String getUNIT_SERVICE_NAME() {
        return UNIT_SERVICE_NAME;
    }

    public String getUNIT_SERVICE_PORT() {
        return UNIT_SERVICE_PORT;
    }

    public String getUNIT_SERVICE_API_LIST_ALL_UNIT() {
        return UNIT_SERVICE_API_LIST_ALL_UNIT;
    }

    public String getUNIT_SERVICE_API_GET_UNIT_BY_ID() {
        return UNIT_SERVICE_API_GET_UNIT_BY_ID;
    }

    public String getUNIT_SERVICE_API_REQUEST_GLOBAL_STATUS() {
        return UNIT_SERVICE_API_REQUEST_GLOBAL_STATUS;
    }

    public String getUNIT_SERVICE_API_MODULE_CONTROL() {
        return UNIT_SERVICE_API_MODULE_CONTROL;
    }

    public String getUNIT_SERVICE_API_ADD_SCHEDULED_EVENT() {
        return UNIT_SERVICE_API_ADD_SCHEDULED_EVENT;
    }

    public String getUNIT_SERVICE_API_DELETE_SCHEDULED_EVENT() {
        return UNIT_SERVICE_API_DELETE_SCHEDULED_EVENT;
    }

    public String getUNIT_SERVICE_API_INCOMING_MESSAGE() {
        return UNIT_SERVICE_API_INCOMING_MESSAGE;
    }

    public String getUNIT_SERVICE_API_GET_UNIT_LOGS() {
        return UNIT_SERVICE_API_GET_UNIT_LOGS;
    }

    public String getSCHEDULER_SERVICE_NAME() {
        return SCHEDULER_SERVICE_NAME;
    }

    public String getSCHEDULER_SERVICE_PORT() {
        return SCHEDULER_SERVICE_PORT;
    }

    public String getSCHEDULER_SERVICE_API_GET_EVENTS_FROM_LIST() {
        return SCHEDULER_SERVICE_API_GET_EVENTS_FROM_LIST;
    }

    public String getSCHEDULER_SERVICE_API_POST_EVENT() {
        return SCHEDULER_SERVICE_API_POST_EVENT;
    }

    public String getSCHEDULER_SERVICE_API_DELETE_EVENT() {
        return SCHEDULER_SERVICE_API_DELETE_EVENT;
    }

    public String getREPORTING_SERVICE_NAME() {
        return REPORTING_SERVICE_NAME;
    }

    public String getREPORTING_SERVICE_PORT() {
        return REPORTING_SERVICE_PORT;
    }

    public String getREPORTING_SERVICE_API_GET_AVERAGES() {
        return REPORTING_SERVICE_API_GET_AVERAGES;
    }

    public String getREPORTING_SERVICE_API_POST_VALUES() {
        return REPORTING_SERVICE_API_POST_VALUES;
    }

}
