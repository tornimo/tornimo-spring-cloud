package io.tornimo.cloud.aws


import spock.lang.Specification

class AwsEcsTornimoPrefixFactorySpock extends Specification {

    def "parses all the data into instance id"() {
        given:
        def input = """{
                        "Cluster": "default",
                        "ContainerInstanceArn": "arn:aws:elasticbeanstalk:us-east-1:123456789012:environment/My App/MyEnvironment",
                        "Version": "parseVersion"
                    }"""

        when:
        def instanceId = AwsEcsInstanceIdFactory.getInstanceId(
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
        instanceId.prefix == "aws.elasticbeanstalk.us-east-1.123456789012.environment.My App.MyEnvironment.default.parseVersion"
    }

    def "takes defaults when data is not parsed"() {
        given:
        def input = """{
                        "Cluster": "parseCluster",
                        "Version": "parseVersion"
                    }"""

        when:
        def instanceId = AwsEcsInstanceIdFactory.getInstanceId(
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
        instanceId.prefix == "partition.service.region.account.resourcetype.resource.qualifier.parseCluster.parseVersion"
    }

    def "takes defaults when data is not fully parsed"() {
        given:
        def input = """{
                        "Cluster": "parseCluster",
                        "ContainerInstanceArn": "arn:aws:s3:::my_corporate_bucket/exampleobjectpng"
                        "Version": "parseVersion"
                    }"""

        when:
        def instanceId = AwsEcsInstanceIdFactory.getInstanceId(
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
        instanceId.prefix == "aws.s3.region.account.my_corporate_bucket.exampleobjectpng.qualifier.parseCluster.parseVersion"
    }

    def "escapes dots"() {
        given:
        def input = """{
                        "Cluster": "parseCluster",
                        "ContainerInstanceArn": "arn:aws:s3:::my_corporate_bucket/exampleobject.png"
                        "Version": "parseVersion"
                    }"""

        when:
        def instanceId = AwsEcsInstanceIdFactory.getInstanceId(
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
        instanceId.prefix == "aws.s3.region.account_acc.my_corporate_bucket.exampleobject_png.qualifier.parseCluster.parseVersion"
    }
}
