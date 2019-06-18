package io.tornimo.cloud;

public class StaticTornimoPrefix implements TornimoPrefix {

    private final String instanceId;

    public StaticTornimoPrefix(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String getPrefix() {
        return instanceId;
    }

    public static TornimoPrefix empty() {
        return new StaticTornimoPrefix("");
    }
}
