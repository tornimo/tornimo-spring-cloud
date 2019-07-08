package io.tornimo.cloud.aws;

import io.tornimo.TornimoEnvironmentData;
import io.tornimo.TornimoStaticEnvironmentData;

public class AwsEc2EnvironmentDataFactory {

    private static final String TOOL_PREFIX = "aws-ec2";

    public static TornimoEnvironmentData getEnvironmentData(AwsEc2MetadataConfig awsEc2MetadataConfig,
                                                            AwsEc2Endpoint awsEc2Endpoint) {
        return getEnvironmentData(awsEc2MetadataConfig, awsEc2Endpoint.metadata());
    }

    public static TornimoEnvironmentData getEnvironmentData(AwsEc2MetadataConfig awsEc2MetadataConfig,
                                                            AwsEc2Metadata awsEc2Metadata) {
        StringBuilder builder = new StringBuilder();

        String region = awsEc2Metadata.getRegion();
        String availabilityZone = awsEc2Metadata.getAvailabilityZone();
        String instanceType = awsEc2Metadata.getInstanceType();
        String instanceId = awsEc2Metadata.getInstanceId();
        String account = awsEc2Metadata.getAccount();

        AppendUtils.appendIfNotEmptyAndEnabled(builder, region, awsEc2MetadataConfig.isRegion(), "region");
        AppendUtils.appendIfNotEmptyAndEnabled(builder, availabilityZone, awsEc2MetadataConfig.isAvailabilityZone(), "availabilityZone");
        AppendUtils.appendIfNotEmptyAndEnabled(builder, account, awsEc2MetadataConfig.isAccount(), "account");
        AppendUtils.appendIfNotEmptyAndEnabled(builder, instanceType, awsEc2MetadataConfig.isInstanceType(), "instanceType");
        AppendUtils.appendIfNotEmptyAndEnabled(builder, instanceId, awsEc2MetadataConfig.isInstanceId(), "instanceId");

        return new TornimoStaticEnvironmentData(AppendUtils.appendPrefix(TOOL_PREFIX, builder.toString()));
    }
}

