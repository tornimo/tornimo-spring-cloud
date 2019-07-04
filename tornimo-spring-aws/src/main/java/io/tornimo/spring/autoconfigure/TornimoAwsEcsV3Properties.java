package io.tornimo.spring.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tornimo.aws-ecs-v3")
public class TornimoAwsEcsV3Properties {

    private final boolean enabled;

    public TornimoAwsEcsV3Properties(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
