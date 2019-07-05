package io.tornimo.spring.autoconfigure

import io.tornimo.TornimoEnvironmentData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@SpringBootTest(classes = [TornimoAwsEcsTestConfig, TornimoAwsEcsV3AutoConfiguration])
@TestPropertySource(properties = "tornimo.aws-ecs-v3.enabled=true")
class TornimoAwsEcsV3AutoConfigurationSpec extends Specification {

    @Autowired(required = false)
    private TornimoEnvironmentData tornimoEnvironmentData

    def "environment data is created"() {
        expect: "the tornimoEnvironmentData is created"
        tornimoEnvironmentData != null
        tornimoEnvironmentData.environmentString == "aws-ecs.region-none.exampleobject_png.qualifier-none"
    }
}
