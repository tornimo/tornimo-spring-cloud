package io.tornimo.cloud.aws


import spock.lang.Specification

class AwsEcsV3EnvironmentDataFactorySpec extends Specification {

    def "parses all the data into instance id"() {
        given:
        def input = metadataExample.replace(
                "arn:aws:ecs:us-east-2:012345678910:task/9781c248-0edd-4cdb-9a93-f63cb662a5d3",
                "arn:aws:elasticbeanstalk:us-east-1:123456789012:environment/My App/MyEnvironment"
        )

        when:
        def environmentData = AwsEcsV3EnvironmentDataFactory.getEnvironmentData(
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
        environmentData.environmentString == "aws.elasticbeanstalk.us-east-1.123456789012.environment.My App.MyEnvironment.default.5"
    }

    def "takes defaults when data is not parsed"() {
        given:
        def input = """{
                        "Cluster": "cluster",
                        "Revision": "revision"
                    }"""

        when:
        def environmentData = AwsEcsV3EnvironmentDataFactory.getEnvironmentData(
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
        environmentData.environmentString == "partition.service.region.account.resourcetype.resource.qualifier.cluster.revision"
    }

    def "takes defaults when data is not fully parsed"() {
        given:
        def input = """{
                        "Cluster": "cluster",
                        "TaskARN": "arn:aws:s3:::my_corporate_bucket/exampleobjectpng"
                        "Revision": "revision"
                    }"""

        when:
        def environmentData = AwsEcsV3EnvironmentDataFactory.getEnvironmentData(
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
        environmentData.environmentString == "aws.s3.region.account.my_corporate_bucket.exampleobjectpng.qualifier.cluster.revision"
    }

    def "escapes dots"() {
        given:
        def input = """{
                        "Cluster": "cluster",
                        "TaskARN": "arn:aws:s3:::my_corporate_bucket/exampleobject.png"
                        "Revision": "revision"
                    }"""

        when:
        def environmentData = AwsEcsV3EnvironmentDataFactory.getEnvironmentData(
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
        environmentData.environmentString == "aws.s3.region.account_acc.my_corporate_bucket.exampleobject_png.qualifier.cluster.revision"
    }

    def metadataExample = """
{
  "Cluster": "default",
  "TaskARN": "arn:aws:ecs:us-east-2:012345678910:task/9781c248-0edd-4cdb-9a93-f63cb662a5d3",
  "Family": "nginx",
  "Revision": "5",
  "DesiredStatus": "RUNNING",
  "KnownStatus": "RUNNING",
  "Containers": [
    {
      "DockerId": "731a0d6a3b4210e2448339bc7015aaa79bfe4fa256384f4102db86ef94cbbc4c",
      "Name": "~internal~ecs~pause",
      "DockerName": "ecs-nginx-5-internalecspause-acc699c0cbf2d6d11700",
      "Image": "amazon/amazon-ecs-pause:0.1.0",
      "ImageID": "",
      "Labels": {
        "com.amazonaws.ecs.cluster": "default",
        "com.amazonaws.ecs.container-name": "~internal~ecs~pause",
        "com.amazonaws.ecs.task-arn": "arn:aws:ecs:us-east-2:012345678910:task/9781c248-0edd-4cdb-9a93-f63cb662a5d3",
        "com.amazonaws.ecs.task-definition-family": "nginx",
        "com.amazonaws.ecs.task-definition-revision": "5"
      },
      "DesiredStatus": "RESOURCES_PROVISIONED",
      "KnownStatus": "RESOURCES_PROVISIONED",
      "Limits": {
        "CPU": 0,
        "Memory": 0
      },
      "CreatedAt": "2018-02-01T20:55:08.366329616Z",
      "StartedAt": "2018-02-01T20:55:09.058354915Z",
      "Type": "CNI_PAUSE",
      "Networks": [
        {
          "NetworkMode": "awsvpc",
          "IPv4Addresses": [
            "10.0.2.106"
          ]
        }
      ]
    },
    {
      "DockerId": "43481a6ce4842eec8fe72fc28500c6b52edcc0917f105b83379f88cac1ff3946",
      "Name": "nginx-curl",
      "DockerName": "ecs-nginx-5-nginx-curl-ccccb9f49db0dfe0d901",
      "Image": "nrdlngr/nginx-curl",
      "ImageID": "sha256:2e00ae64383cfc865ba0a2ba37f61b50a120d2d9378559dcd458dc0de47bc165",
      "Labels": {
        "com.amazonaws.ecs.cluster": "default",
        "com.amazonaws.ecs.container-name": "nginx-curl",
        "com.amazonaws.ecs.task-arn": "arn:aws:ecs:us-east-2:012345678910:task/9781c248-0edd-4cdb-9a93-f63cb662a5d3",
        "com.amazonaws.ecs.task-definition-family": "nginx",
        "com.amazonaws.ecs.task-definition-revision": "5"
      },
      "DesiredStatus": "RUNNING",
      "KnownStatus": "RUNNING",
      "Limits": {
        "CPU": 512,
        "Memory": 512
      },
      "CreatedAt": "2018-02-01T20:55:10.554941919Z",
      "StartedAt": "2018-02-01T20:55:11.064236631Z",
      "Type": "NORMAL",
      "Networks": [
        {
          "NetworkMode": "awsvpc",
          "IPv4Addresses": [
            "10.0.2.106"
          ]
        }
      ]
    }
  ],
  "PullStartedAt": "2018-02-01T20:55:09.372495529Z",
  "PullStoppedAt": "2018-02-01T20:55:10.552018345Z",
  "AvailabilityZone": "us-east-2b"
}
"""
}
