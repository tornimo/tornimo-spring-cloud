package io.tornimo.cloud.aws


import spock.lang.Specification

class AwsEc2EnvironmentDataFactorySpec extends Specification {

    def "fails if any requested element is empty"() {
        given:
        def conf = AwsEc2MetadataConfig.defaults()
        def metadata = new AwsEc2Metadata("reg", "", "itype", "iid", "acc")

        when:
        AwsEc2EnvironmentDataFactory.getEnvironmentData(conf, metadata)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "availabilityZone is missing and no default value is provided"
    }

    def "enables availability zone, instance type, and instance ID by default"() {
        given:
        def conf = AwsEc2MetadataConfig.defaults()
        def metadata = new AwsEc2Metadata("reg", "az", "itype", "iid", "")

        when:
        def data = AwsEc2EnvironmentDataFactory.getEnvironmentData(conf, metadata)

        then:
        data.environmentString == "aws-ec2.az.itype.iid"
    }

    def "generates account id as third element"() {
        given:
        def conf = AwsEc2MetadataConfig.full()
        def metadata = new AwsEc2Metadata("reg", "az", "itype", "iid", "acc")

        when:
        def data = AwsEc2EnvironmentDataFactory.getEnvironmentData(conf, metadata)

        then:
        data.environmentString == "aws-ec2.reg.az.acc.itype.iid"
    }

    def "always appends aws-ec2 as prefix"() {
        given:
        def conf = AwsEc2MetadataConfig.none()
        def metadata = new AwsEc2Metadata("reg", "az", "itype", "iid", "acc")

        when:
        def data = AwsEc2EnvironmentDataFactory.getEnvironmentData(conf, metadata)

        then:
        data.environmentString == "aws-ec2"
    }
}
