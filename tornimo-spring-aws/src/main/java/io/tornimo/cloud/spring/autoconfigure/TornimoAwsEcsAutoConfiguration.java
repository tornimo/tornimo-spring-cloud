package io.tornimo.cloud.spring.autoconfigure;

import io.tornimo.spring.autoconfigure.TornimoMetricsExportAutoConfiguration;
import io.tornimo.spring.autoconfigure.TornimoProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore({
        TornimoMetricsExportAutoConfiguration.class
})
@ConditionalOnProperty(
        prefix = "tornimo.aws.ecs",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false)
@EnableConfigurationProperties(TornimoProperties.class)
public class TornimoAwsEcsAutoConfiguration {

//    @ConfigurationProperties(prefix = "tornimo.aws.ecs")
//    private final AwsEcsMetadataConfig awsEcsMetadataConfig;

//    @Bean
//    @ConditionalOnMissingBean
//    public InstanceId instanceId(TornimoPathProperties pathProperties) {
//        return AwsEcsInstanceIdFactory.getInstanceId(null, pathProperties);
//    }
}
