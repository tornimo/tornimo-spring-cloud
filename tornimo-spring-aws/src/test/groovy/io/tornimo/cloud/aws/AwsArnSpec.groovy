package io.tornimo.cloud.aws

import spock.lang.Specification

class AwsArnSpec extends Specification {
    /**
     * Source of examples: https://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html
     */

    def "parses arn string, example A"() {
        given:
        def input = "arn:aws:elasticbeanstalk:us-east-1:123456789012:environment/My App/MyEnvironment"

        when:
        def arn = AwsArn.fromString(input)

        then:
        arn.getPartition() == "aws"
        arn.getService() == "elasticbeanstalk"
        arn.getRegion() == "us-east-1"
        arn.getAccount() == "123456789012"
        arn.getResourceType() == "environment"
        arn.getResource() == "My App"
        arn.getQualifier() == "MyEnvironment"
    }

    def "parses arn string, example B"() {
        given:
        def input = "arn:aws:iam::123456789012:user/David"

        when:
        def arn = AwsArn.fromString(input)

        then:
        arn.getPartition() == "aws"
        arn.getService() == "iam"
        arn.getRegion() == ""
        arn.getAccount() == "123456789012"
        arn.getResourceType() == "user"
        arn.getResource() == "David"
        arn.getQualifier() == ""
    }

    def "parses arn string, example C"() {
        given:
        def input = "arn:aws:rds:eu-west-1:123456789012:db:mysql-db"

        when:
        def arn = AwsArn.fromString(input)

        then:
        arn.getPartition() == "aws"
        arn.getService() == "rds"
        arn.getRegion() == "eu-west-1"
        arn.getAccount() == "123456789012"
        arn.getResourceType() == "db"
        arn.getResource() == "mysql-db"
        arn.getQualifier() == ""
    }

    def "parses arn string, example D"() {
        given:
        def input = "arn:aws:s3:::my_corporate_bucket/exampleobject.png"

        when:
        def arn = AwsArn.fromString(input)

        then:
        arn.getPartition() == "aws"
        arn.getService() == "s3"
        arn.getRegion() == ""
        arn.getAccount() == ""
        arn.getResourceType() == "my_corporate_bucket"
        arn.getResource() == "exampleobject.png"
        arn.getQualifier() == ""
    }
}
