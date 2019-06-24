package io.tornimo.cloud;

import io.tornimo.spring.autoconfigure.TornimoEnvironmentData;

public class StaticEnvironmentData implements TornimoEnvironmentData {

    private final String data;

    public StaticEnvironmentData(String data) {
        this.data = data;
    }

    public String getEnvironmentString() {
        return data;
    }

    public static TornimoEnvironmentData empty() {
        return new StaticEnvironmentData("");
    }
}
