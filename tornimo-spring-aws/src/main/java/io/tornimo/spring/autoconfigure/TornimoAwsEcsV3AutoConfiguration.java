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
        boolean partition = false;
        boolean service = false;
        boolean region = true;
        boolean account = false;
        boolean resourceType = true;
        boolean resource = true;
        boolean qualifier = true;
        String defaultPartition = "";
        String defaultService = "";
        String defaultRegion = "region-none";
        String defaultAccount = "";
        String defaultResourceType = "resourcetype-none";
        String defaultResource = "resource-none";
        String defaultQualifier = "qualifier-none";

        return new AwsArnConfig(
                partition,
                service,
                region,
                account,
                resourceType,
                resource,
                qualifier,
                defaultPartition,
                defaultService,
                defaultRegion,
                defaultAccount,
                defaultResourceType,
                defaultResource,
                defaultQualifier
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public TornimoEnvironmentData tornimoEnvironmentData(AwsArnConfig awsArnConfig,
                                                         AwsEcsMetadataConfig awsEcsMetadataConfig) {
        return AwsEcsV3EnvironmentDataFactory.getEnvironmentData(awsEcsMetadataConfig, awsArnConfig);
    }
}
