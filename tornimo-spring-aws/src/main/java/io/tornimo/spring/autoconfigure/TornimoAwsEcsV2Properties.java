package io.tornimo.spring.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tornimo.aws-ecs-v2")
public class TornimoAwsEcsV2Properties {

    private boolean enabled;

    public TornimoAwsEcsV2Properties() {
        this(false);
    }

    public TornimoAwsEcsV2Properties(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
