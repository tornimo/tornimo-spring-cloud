package io.tornimo.cloud;

public class StaticInstanceId implements InstanceId {

    private final String instanceId;

    public StaticInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String getInstanceId() {
        return instanceId;
    }

    public static InstanceId empty() {
        return new StaticInstanceId("");
    }
}
