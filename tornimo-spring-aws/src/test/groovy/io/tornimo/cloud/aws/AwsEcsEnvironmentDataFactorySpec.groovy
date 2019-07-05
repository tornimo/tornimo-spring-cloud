package io.tornimo.cloud.aws


import spock.lang.Specification

class AwsEcsEnvironmentDataFactorySpec extends Specification {

    def "parses all the data into instance id"() {
        given:
        def input = AwsEcsV3Metadata.EXAMPLE.replace(
                "arn:aws:ecs:us-east-2:012345678910:task/9781c248-0edd-4cdb-9a93-f63cb662a5d3",
                "arn:aws:elasticbeanstalk:us-east-1:123456789012:environment/My App/MyEnvironment"
        )

        when:
        def environmentData = AwsEcsEnvironmentDataFactory.getEnvironmentData(
                new AwsEcsMetadataConfig(
                        true,
                        true,
                        true),
                new AwsArnConfig(
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                ),
                input
        )

        then:
        environmentData.environmentString == "aws-ecs.aws.elasticbeanstalk.us-east-1.123456789012.environment.My App.MyEnvironment.default.5"
    }

    def "takes defaults when data is not parsed"() {
        given:
        def input = """{
                        "Cluster": "cluster",
                        "Revision": "revision"
                    }"""

        when:
        def environmentData = AwsEcsEnvironmentDataFactory.getEnvironmentData(
                new AwsEcsMetadataConfig(
                        true,
                        true,
                        false),
                new AwsArnConfig(
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        "partition",
                        "service",
                        "region",
                        "account",
                        "resourcetype",
                        "resource",
                        "qualifier"
                ),
                input
        )

        then:
        environmentData.environmentString == "aws-ecs.partition.service.region.account.resourcetype.resource.qualifier.cluster.revision"
    }

    def "takes defaults when data is not fully parsed"() {
        given:
        def input = """{
                        "Cluster": "cluster",
                        "TaskARN": "arn:aws:s3:::my_corporate_bucket/exampleobjectpng"
                        "Revision": "revision"
                    }"""

        when:
        def environmentData = AwsEcsEnvironmentDataFactory.getEnvironmentData(
                new AwsEcsMetadataConfig(
                        true,
                        true,
                        true),
                new AwsArnConfig(
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        "partition",
                        "service",
                        "region",
                        "account",
                        "resourcetype",
                        "resource",
                        "qualifier"
                ),
                input
        )

        then:
        environmentData.environmentString == "aws-ecs.aws.s3.region.account.my_corporate_bucket.exampleobjectpng.qualifier.cluster.revision"
    }

    def "escapes dots"() {
        given:
        def input = """{
                        "Cluster": "cluster",
                        "TaskARN": "arn:aws:s3:::my_corporate_bucket/exampleobject.png"
                        "Revision": "revision"
                    }"""

        when:
        def environmentData = AwsEcsEnvironmentDataFactory.getEnvironmentData(
                new AwsEcsMetadataConfig(
                        true,
                        true,
                        true),
                new AwsArnConfig(
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        "partition",
                        "service",
                        "region",
                        "account.acc",
                        "resourcetype",
                        "resource",
                        "qualifier"
                ),
                input
        )

        then:
        environmentData.environmentString == "aws-ecs.aws.s3.region.account_acc.my_corporate_bucket.exampleobject_png.qualifier.cluster.revision"
    }

    def "queries endpoint and returns expected result"() {
        given:
        def input = """{
                        "Cluster": "cluster",
                        "TaskARN": "arn:aws:s3:::my_corporate_bucket/exampleobject.png"
                        "Revision": "revision"
                    }"""
        when:
        def environmentData = AwsEcsEnvironmentDataFactory.getEnvironmentData(
                new AwsEcsMetadataConfig(
                        true,
                        true,
                        true),
                new AwsArnConfig(
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        "partition",
                        "service",
                        "region",
                        "account.acc",
                        "resourcetype",
                        "resource",
                        "qualifier"
                ),
                new AwsEndpoint() {
                    @Override
                    String query() {
                        return input
                    }
                }
        )

        then:
        environmentData.environmentString == "aws-ecs.aws.s3.region.account_acc.my_corporate_bucket.exampleobject_png.qualifier.cluster.revision"
    }
}
