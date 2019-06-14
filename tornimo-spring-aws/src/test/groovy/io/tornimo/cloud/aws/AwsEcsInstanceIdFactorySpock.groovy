package io.tornimo.cloud.aws


import spock.lang.Specification

class AwsEcsInstanceIdFactorySpock extends Specification {

    def "parses all the data into instance id"() {
        given:
        def input = """{
                        "Cluster": "default",
                        "ContainerInstanceArn": "arn:aws:elasticbeanstalk:us-east-1:123456789012:environment/My App/MyEnvironment",
                        "Version": "version"
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
        instanceId.instanceId == "aws.elasticbeanstalk.us-east-1.123456789012.environment.My App.MyEnvironment.default.version"
    }

    def "takes defaults when data is not parsed"() {
        given:
        def input = """{
                        "Cluster": "cluster",
                        "Version": "version"
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
        instanceId.instanceId == "partition.service.region.account.resourcetype.resource.qualifier.cluster.version"
    }

    def "takes defaults when data is not fully parsed"() {
        given:
        def input = """{
                        "Cluster": "cluster",
                        "ContainerInstanceArn": "arn:aws:s3:::my_corporate_bucket/exampleobjectpng"
                        "Version": "version"
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
        instanceId.instanceId == "aws.s3.region.account.my_corporate_bucket.exampleobjectpng.qualifier.cluster.version"
    }

    def "escapes dots"() {
        given:
        def input = """{
                        "Cluster": "cluster",
                        "ContainerInstanceArn": "arn:aws:s3:::my_corporate_bucket/exampleobject.png"
                        "Version": "version"
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
        instanceId.instanceId == "aws.s3.region.account_acc.my_corporate_bucket.exampleobject_png.qualifier.cluster.version"
    }
}
