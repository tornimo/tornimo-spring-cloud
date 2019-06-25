package io.tornimo.spring.autoconfigure;

import io.tornimo.TornimoEnvironmentData;
import io.tornimo.cloud.aws.AwsArnConfig;
import io.tornimo.cloud.aws.AwsEcsMetadataConfig;
import io.tornimo.cloud.aws.AwsEcsV3EnvironmentDataFactory;
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
        prefix = "tornimo.aws.ecs.v3",
        name = "enabled",
        havingValue = "true")
@EnableConfigurationProperties(TornimoProperties.class)
public class TornimoAwsEcsV3AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AwsEcsMetadataConfig awsEcsMetadataConfig() {
        return new AwsEcsMetadataConfig(false, true, true);
    }

    @Bean
    @ConditionalOnMissingBean
    public AwsArnConfig awsArnConfig() {
        return new AwsArnConfig(
                false,
                false,
                true,
                false,
                true,
                true,
                true,
                "",
                "",
                "region-none",
                "",
                "resourcetype-none",
                "resource-none",
                "qualifier-none"
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public TornimoEnvironmentData tornimoEnvironmentData(AwsArnConfig awsArnConfig,
                                                         AwsEcsMetadataConfig awsEcsMetadataConfig) {
        return AwsEcsV3EnvironmentDataFactory.getEnvironmentData(awsEcsMetadataConfig, awsArnConfig);
    }
}
