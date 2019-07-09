package io.tornimo.spring.autoconfigure

import io.tornimo.TornimoEnvironmentData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@SpringBootTest(classes = [TornimoAwsEcsTestConfig, TornimoAwsEc2AutoConfiguration])
@TestPropertySource(properties = "tornimo.aws-ec2.enabled=true")
class TornimoAwsEc2AutoConfigurationSpec extends Specification {

    @Autowired(required = false)
    private TornimoEnvironmentData tornimoEnvironmentData

    def "environment data is created"() {
        expect: "the tornimoEnvironmentData is created"
        tornimoEnvironmentData != null
        tornimoEnvironmentData.environmentString == "aws-ec2.region.availability-zone.instance-type.instance-id"
    }
}
