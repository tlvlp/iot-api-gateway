package com.tlvlp.iot.server.api.gateway.services;

import java.util.List;

public class UnitModules {

    private String unitID;
    private List<Object> modules;

    @Override
    public String toString() {
        return "UnitModules{" +
                "unitID='" + unitID + '\'' +
                ", modules=" + modules +
                '}';
    }

    public String getUnitID() {
        return unitID;
    }

    public UnitModules setUnitID(String unitID) {
        this.unitID = unitID;
        return this;
    }

    public List<Object> getModules() {
        return modules;
    }

    public UnitModules setModules(List<Object> modules) {
        this.modules = modules;
        return this;
    }
}
