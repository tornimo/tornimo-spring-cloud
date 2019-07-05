package io.tornimo.spring.autoconfigure

import io.tornimo.cloud.aws.AwsEndpoint
import org.springframework.context.annotation.Bean

class TornimoAwsEcsTestConfig {
    @Bean
    AwsEndpoint tornimoAwsEndpoint() {
        return new AwsEndpoint() {
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
}
