package io.tornimo.spring.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tornimo.aws-ec2")
public class TornimoAwsEc2Properties {

    private boolean enabled;

    public TornimoAwsEc2Properties() {
        this(false);
    }

    public TornimoAwsEc2Properties(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
