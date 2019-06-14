package io.tornimo.cloud.aws

import spock.lang.Specification

class AwsEcsMetadataSpec extends Specification {

    def "parses basic data"() {
        given:
        def input = """{
                        "Cluster": "default",
                        "ContainerInstanceArn": "arn:aws:s3:::my_corporate_bucket/exampleobject.png",
                        "Version": "Amazon ECS Agent - v1.29.0 (a190a73f)"
                    }"""
        def parseArn = true
        def parseCluster = true
        def parsVersion = true

        when:
        def metadata = AwsEcsMetadata.formJson(input, parseCluster, parsVersion, parseArn)

        then:
        metadata.getCluster() == "default"
        metadata.getVersion() == "Amazon ECS Agent - v1.29.0 (a190a73f)"
        !metadata.getAwsArn().isEmpty()
    }

    def "ignores parsing"() {
        given:
        def input = """{
                        "Cluster": "default",
                        "ContainerInstanceArn": "arn:aws:s3:::my_corporate_bucket/exampleobject.png",
                        "Version": "Amazon ECS Agent - v1.29.0 (a190a73f)"
                    }"""
        def parseArn = false
        def parseCluster = false
        def parsVersion = false

        when:
        def metadata = AwsEcsMetadata.formJson(input, parseCluster, parsVersion, parseArn)

        then:
        metadata.getCluster() == ""
        metadata.getVersion() == ""
        metadata.getAwsArn().isEmpty()
    }

    def "fails when arn is invalid"() {
        given:
        def input = """{
                        "Cluster": "default",
                        "ContainerInstanceArn": "invalid arn",
                        "Version": "Amazon ECS Agent - v1.29.0 (a190a73f)"
                    }"""
        def parseArn = true
        def parseCluster = false
        def parsVersion = false

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsVersion, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "arn \"invalid arn\" could not be parsed."
    }

    def "ignores missing fields when parsing disabled"() {
        given:
        def input = """{
                    }"""
        def parseArn = false
        def parseCluster = false
        def parsVersion = false

        when:
        def metadata = AwsEcsMetadata.formJson(input, parseCluster, parsVersion, parseArn)

        then:
        metadata.getCluster() == ""
        metadata.getVersion() == ""
        metadata.getAwsArn().isEmpty()
    }

    def "fails on missing fields when parseArn is true"() {
        given:
        def input = """{"NoContainerInstanceArn":"no"}"""
        def parseArn = true
        def parseCluster = false
        def parsVersion = false

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsVersion, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "\"ContainerInstanceArn\" not found in \"{\"NoContainerInstanceArn\":\"no\"}\""
    }

    def "fails on missing fields when parseVersion is true"() {
        given:
        def input = """{"NoVersion":"no"}"""
        def parseArn = false
        def parseCluster = false
        def parsVersion = true

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsVersion, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "\"Version\" not found in \"{\"NoVersion\":\"no\"}\""
    }

    def "fails on missing fields when parseCluster is true"() {
        given:
        def input = """{"NoCluster":"no"}"""
        def parseArn = false
        def parseCluster = true
        def parsVersion = false

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsVersion, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "\"Cluster\" not found in \"{\"NoCluster\":\"no\"}\""
    }

    def "fails on empty fields when parseArn is true"() {
        given:
        def input = """{"ContainerInstanceArn":""}"""
        def parseArn = true
        def parseCluster = false
        def parsVersion = false

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsVersion, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "\"ContainerInstanceArn\" is empty in \"{\"ContainerInstanceArn\":\"\"}\""
    }

    def "fails on empty fields when parseVersion is true"() {
        given:
        def input = """{"Version":""}"""
        def parseArn = false
        def parseCluster = false
        def parsVersion = true

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsVersion, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "\"Version\" is empty in \"{\"Version\":\"\"}\""
    }

    def "fails on empty fields when parseCluster is true"() {
        given:
        def input = """{"Cluster":""}"""
        def parseArn = false
        def parseCluster = true
        def parsVersion = false

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsVersion, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "\"Cluster\" is empty in \"{\"Cluster\":\"\"}\""
    }
}
