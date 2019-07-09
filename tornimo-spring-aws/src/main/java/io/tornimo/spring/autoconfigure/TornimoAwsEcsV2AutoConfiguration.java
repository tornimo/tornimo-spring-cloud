package io.tornimo.spring.autoconfigure;

import io.tornimo.TornimoEnvironmentData;
import io.tornimo.cloud.aws.*;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore({
        TornimoMetricsExportAutoConfiguration.class
})
@ConditionalOnProperty(
        prefix = "tornimo.aws-ecs-v2",
        name = "enabled",
        havingValue = "true")
@EnableConfigurationProperties(TornimoAwsEcsV2Properties.class)
public class TornimoAwsEcsV2AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AwsEcsMetadataConfig awsEcsMetadataConfig() {
        return new AwsEcsMetadataConfig(false, false, true);
    }

    @Bean
    @ConditionalOnMissingBean
    public AwsArnConfig awsArnConfig() {
        return TornimoAwsEcsDefaults.getDefaultArnConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public TornimoEnvironmentData tornimoEnvironmentData(AwsArnConfig awsArnConfig,
                                                         AwsEcsMetadataConfig awsEcsMetadataConfig,
                                                         AwsEcsEndpoint awsEcsEndpoint) {
        return AwsEcsEnvironmentDataFactory.getEnvironmentData(awsEcsMetadataConfig, awsArnConfig, awsEcsEndpoint);
    }

    @Bean
    @ConditionalOnMissingBean
    public AwsEcsEndpoint tornimoAwsEndpoint() {
        return new AwsEcsV2Endpoint();
    }
}
