package io.tornimo.cloud.aws

import spock.lang.Specification

class AwsEcsMetadataSpec extends Specification {

    def "parses basic data"() {
        given:
        def input = """{
                        "Cluster": "default",
                        "TaskARN": "arn:aws:s3:::my_corporate_bucket/exampleobject.png",
                        "Revision": "Amazon ECS Agent - v1.29.0 (a190a73f)"
                    }"""
        def parseArn = true
        def parseCluster = true
        def parsRevision = true

        when:
        def metadata = AwsEcsMetadata.formJson(input, parseCluster, parsRevision, parseArn)

        then:
        metadata.getCluster() == "default"
        metadata.getRevision() == "Amazon ECS Agent - v1.29.0 (a190a73f)"
        !metadata.getAwsArn().isEmpty()
    }

    def "ignores parsing"() {
        given:
        def input = """{
                        "Cluster": "default",
                        "TaskARN": "arn:aws:s3:::my_corporate_bucket/exampleobject.png",
                        "Revision": "Amazon ECS Agent - v1.29.0 (a190a73f)"
                    }"""
        def parseArn = false
        def parseCluster = false
        def parsRevision = false

        when:
        def metadata = AwsEcsMetadata.formJson(input, parseCluster, parsRevision, parseArn)

        then:
        metadata.getCluster() == ""
        metadata.getRevision() == ""
        metadata.getAwsArn().isEmpty()
    }

    def "fails when arn is invalid"() {
        given:
        def input = """{
                        "Cluster": "default",
                        "TaskARN": "invalid arn",
                        "Revision": "Amazon ECS Agent - v1.29.0 (a190a73f)"
                    }"""
        def parseArn = true
        def parseCluster = false
        def parsRevision = false

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsRevision, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == """arn "invalid arn" could not be parsed."""
    }

    def "ignores missing fields when parsing disabled"() {
        given:
        def input = """{
                    }"""
        def parseArn = false
        def parseCluster = false
        def parsRevision = false

        when:
        def metadata = AwsEcsMetadata.formJson(input, parseCluster, parsRevision, parseArn)

        then:
        metadata.getCluster() == ""
        metadata.getRevision() == ""
        metadata.getAwsArn().isEmpty()
    }

    def "fails on missing fields when parseArn is true"() {
        given:
        def input = """{"NoTaskARN":"no"}"""
        def parseArn = true
        def parseCluster = false
        def parsRevision = false

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsRevision, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == '"TaskARN" not found in "{"NoTaskARN":"no"}"'
    }

    def "fails on missing fields when parseRevision is true"() {
        given:
        def input = """{"NoRevision":"no"}"""
        def parseArn = false
        def parseCluster = false
        def parsRevision = true

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsRevision, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == '"Revision" not found in "{"NoRevision":"no"}"'
    }

    def "fails on missing fields when parseCluster is true"() {
        given:
        def input = """{"NoCluster":"no"}"""
        def parseArn = false
        def parseCluster = true
        def parsRevision = false

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsRevision, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == '"Cluster" not found in "{"NoCluster":"no"}"'
    }

    def "fails on empty fields when parseArn is true"() {
        given:
        def input = """{"TaskARN":""}"""
        def parseArn = true
        def parseCluster = false
        def parsRevision = false

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsRevision, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == '"TaskARN" is empty in "{"TaskARN":""}"'
    }

    def "fails on empty fields when parseRevision is true"() {
        given:
        def input = """{"Revision":""}"""
        def parseArn = false
        def parseCluster = false
        def parsRevision = true

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsRevision, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == '"Revision" is empty in "{"Revision":""}"'
    }

    def "fails on empty fields when parseCluster is true"() {
        given:
        def input = """{"Cluster":""}"""
        def parseArn = false
        def parseCluster = true
        def parsRevision = false

        when:
        AwsEcsMetadata.formJson(input, parseCluster, parsRevision, parseArn)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == '"Cluster" is empty in "{"Cluster":""}"'
    }
}
