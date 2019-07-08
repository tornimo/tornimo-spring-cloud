package io.tornimo.spring.autoconfigure;

import io.tornimo.TornimoEnvironmentData;
import io.tornimo.cloud.aws.AwsEc2Endpoint;
import io.tornimo.cloud.aws.AwsEc2EnvironmentDataFactory;
import io.tornimo.cloud.aws.AwsEc2Metadata;
import io.tornimo.cloud.aws.AwsEc2MetadataConfig;
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
        prefix = "tornimo.aws-ec2",
        name = "enabled",
        havingValue = "true")
@EnableConfigurationProperties(TornimoAwsEc2Properties.class)
public class TornimoAwsEc2AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AwsEc2MetadataConfig awsEc2MetadataConfig() {
        return AwsEc2MetadataConfig.defaults();
    }

    @Bean
    @ConditionalOnMissingBean
    public TornimoEnvironmentData tornimoEnvironmentData(AwsEc2MetadataConfig awsEc2MetadataConfig,
                                                         AwsEc2Endpoint awsEc2Endpoint) {
        return AwsEc2EnvironmentDataFactory.getEnvironmentData(awsEc2MetadataConfig, awsEc2Endpoint);
    }

    @Bean
    @ConditionalOnMissingBean
    public AwsEc2Endpoint tornimoAwsEndpoint() {
        return AwsEc2Metadata::create;
    }
}
