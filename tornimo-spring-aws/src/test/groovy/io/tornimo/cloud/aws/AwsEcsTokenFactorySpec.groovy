package io.tornimo.cloud.aws

import io.tornimo.cloud.spring.autoconfigure.TornimoPathProperties
import io.tornimo.tokens.TornimoPathTokens
import io.tornimo.tokens.TornimoRequestedTokens
import spock.lang.Specification

class AwsEcsTokenFactorySpec extends Specification {

    def "parses all the data into tokens"() {
        given:
        def input = """{
                        "Cluster": "default",
                        "ContainerInstanceArn": "arn:aws:elasticbeanstalk:us-east-1:123456789012:environment/My App/MyEnvironment",
                        "Version": "parseVersion"
                    }"""

        when:
        def values = AwsEcsTokenFactory.getTokens(
                TornimoRequestedTokens.fromTornimoPathTokens(
                        TornimoPathTokens.fromString(
                                "{${AwsEcsTokenFactory.AWS_ECS_CLUSTER}}." +
                                        "{${AwsEcsTokenFactory.AWS_ECS_VERSION}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_PARTITION}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_ACCOUNT}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_QUALIFIER}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_REGION}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_RESOURCE}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_RESOURCETYPE}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_SERVICE}}")
                ),
                input
        )

        then:
        values.getTokenMap() == [
                (AwsEcsTokenFactory.AWS_ECS_CLUSTER)     : "default",
                (AwsEcsTokenFactory.AWS_ECS_VERSION)     : "parseVersion",
                (AwsEcsTokenFactory.AWS_ARN_PARTITION)   : "aws",
                (AwsEcsTokenFactory.AWS_ARN_ACCOUNT)     : "123456789012",
                (AwsEcsTokenFactory.AWS_ARN_QUALIFIER)   : "MyEnvironment",
                (AwsEcsTokenFactory.AWS_ARN_REGION)      : "us-east-1",
                (AwsEcsTokenFactory.AWS_ARN_RESOURCE)    : "My App",
                (AwsEcsTokenFactory.AWS_ARN_RESOURCETYPE): "environment",
                (AwsEcsTokenFactory.AWS_ARN_SERVICE)     : "elasticbeanstalk",
        ]
    }

    def "does not parse ContainerInstanceArn data when it is not in request"() {
        given:
        def input = """{
                        "Cluster": "parseCluster",
                        "Version": "parseVersion"
                    }"""

        when:
        def values = AwsEcsTokenFactory.getTokens(
                TornimoRequestedTokens.fromTornimoPathTokens(
                        TornimoPathTokens.fromString(
                                "{${AwsEcsTokenFactory.AWS_ECS_CLUSTER}}." +
                                        "{${AwsEcsTokenFactory.AWS_ECS_VERSION}}")
                ),
                input
        )

        then:
        values.getTokenMap() == [
                (AwsEcsTokenFactory.AWS_ECS_CLUSTER): "parseCluster",
                (AwsEcsTokenFactory.AWS_ECS_VERSION): "parseVersion"
        ]
    }

    def "does not parse Cluster data when it is not in request"() {
        given:
        def input = """{
                        "Version": "parseVersion",
                        "ContainerInstanceArn": "arn:aws:elasticbeanstalk:us-east-1:123456789012:environment/My App/MyEnvironment",
                    }"""

        when:
        def values = AwsEcsTokenFactory.getTokens(
                TornimoRequestedTokens.fromTornimoPathTokens(
                        TornimoPathTokens.fromString(
                                "{${AwsEcsTokenFactory.AWS_ECS_VERSION}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_PARTITION}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_ACCOUNT}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_QUALIFIER}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_REGION}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_RESOURCE}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_RESOURCETYPE}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_SERVICE}}")
                ),
                input
        )

        then:
        values.getTokenMap() == [
                (AwsEcsTokenFactory.AWS_ECS_VERSION)     : "parseVersion",
                (AwsEcsTokenFactory.AWS_ARN_PARTITION)   : "aws",
                (AwsEcsTokenFactory.AWS_ARN_ACCOUNT)     : "123456789012",
                (AwsEcsTokenFactory.AWS_ARN_QUALIFIER)   : "MyEnvironment",
                (AwsEcsTokenFactory.AWS_ARN_REGION)      : "us-east-1",
                (AwsEcsTokenFactory.AWS_ARN_RESOURCE)    : "My App",
                (AwsEcsTokenFactory.AWS_ARN_RESOURCETYPE): "environment",
                (AwsEcsTokenFactory.AWS_ARN_SERVICE)     : "elasticbeanstalk",
        ]
    }

    def "does not parse Version data when it is not in request"() {
        given:
        def input = """{
                        "Cluster": "parseCluster",
                        "ContainerInstanceArn": "arn:aws:elasticbeanstalk:us-east-1:123456789012:environment/My App/MyEnvironment",
                    }"""

        when:
        def values = AwsEcsTokenFactory.getTokens(
                TornimoRequestedTokens.fromTornimoPathTokens(
                        TornimoPathTokens.fromString(
                                "{${AwsEcsTokenFactory.AWS_ECS_CLUSTER}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_PARTITION}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_ACCOUNT}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_QUALIFIER}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_REGION}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_RESOURCE}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_RESOURCETYPE}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_SERVICE}}")
                ),
                input
        )

        then:
        values.getTokenMap() == [
                (AwsEcsTokenFactory.AWS_ECS_CLUSTER)     : "parseCluster",
                (AwsEcsTokenFactory.AWS_ARN_PARTITION)   : "aws",
                (AwsEcsTokenFactory.AWS_ARN_ACCOUNT)     : "123456789012",
                (AwsEcsTokenFactory.AWS_ARN_QUALIFIER)   : "MyEnvironment",
                (AwsEcsTokenFactory.AWS_ARN_REGION)      : "us-east-1",
                (AwsEcsTokenFactory.AWS_ARN_RESOURCE)    : "My App",
                (AwsEcsTokenFactory.AWS_ARN_RESOURCETYPE): "environment",
                (AwsEcsTokenFactory.AWS_ARN_SERVICE)     : "elasticbeanstalk",
        ]
    }

    def "takes defaults when data is not fully parsed"() {
        given:
        def input = """{
                        "Cluster": "parseCluster",
                        "ContainerInstanceArn": "arn:aws:s3:::my_corporate_bucket/exampleobjectpng"
                        "Version": "parseVersion"
                    }"""

        when:
        def values = AwsEcsTokenFactory.getTokens(
                TornimoRequestedTokens.fromTornimoPathTokens(
                        TornimoPathTokens.fromString(
                                "{${AwsEcsTokenFactory.AWS_ECS_CLUSTER}}." +
                                        "{${AwsEcsTokenFactory.AWS_ECS_VERSION}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_PARTITION}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_ACCOUNT}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_QUALIFIER}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_REGION}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_RESOURCE}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_RESOURCETYPE}}." +
                                        "{${AwsEcsTokenFactory.AWS_ARN_SERVICE}}")
                ),
                input
        )

        then:
        values.getTokenMap() == [
                (AwsEcsTokenFactory.AWS_ECS_CLUSTER)     : "parseCluster",
                (AwsEcsTokenFactory.AWS_ECS_VERSION)     : "parseVersion",
                (AwsEcsTokenFactory.AWS_ARN_PARTITION)   : "aws",
                (AwsEcsTokenFactory.AWS_ARN_ACCOUNT)     : TornimoPathProperties.DEFAULT_VALUE,
                (AwsEcsTokenFactory.AWS_ARN_QUALIFIER)   : TornimoPathProperties.DEFAULT_VALUE,
                (AwsEcsTokenFactory.AWS_ARN_REGION)      : TornimoPathProperties.DEFAULT_VALUE,
                (AwsEcsTokenFactory.AWS_ARN_RESOURCE)    : "exampleobjectpng",
                (AwsEcsTokenFactory.AWS_ARN_RESOURCETYPE): "my_corporate_bucket",
                (AwsEcsTokenFactory.AWS_ARN_SERVICE)     : "s3",
        ]
    }
}
