package io.tornimo.spring.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tornimo.aws-ecs-v3")
public class TornimoAwsEcsV3Properties {

    private boolean enabled;

    public TornimoAwsEcsV3Properties() {
        this(false);
    }

    public TornimoAwsEcsV3Properties(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
