package io.tornimo.spring.autoconfigure

import io.tornimo.cloud.aws.AwsEc2Endpoint
import io.tornimo.cloud.aws.AwsEc2Metadata
import io.tornimo.cloud.aws.AwsEcsEndpoint
import org.springframework.context.annotation.Bean

class TornimoAwsEcsTestConfig {
    @Bean
    AwsEcsEndpoint tornimoAwsEcsEndpoint() {
        return new AwsEcsEndpoint() {
            @Override
            String query() {
                return """{
                        "Cluster": "cluster",
                        "TaskARN": "arn:aws:s3:::my_corporate_bucket/exampleobject.png"
                        "Revision": "revision"
                    }"""
            }
        }
    }

    @Bean
    AwsEc2Endpoint tornimoAwsEc2Endpoint() {
        return { ->
            new AwsEc2Metadata(
                    "region", "availability-zone", "instance-type", "instance-id", "account"
            )
        }
    }
}
