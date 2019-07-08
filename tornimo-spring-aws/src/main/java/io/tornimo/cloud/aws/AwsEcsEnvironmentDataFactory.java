package io.tornimo.cloud.aws;

import io.tornimo.TornimoEnvironmentData;
import io.tornimo.TornimoStaticEnvironmentData;

public class AwsEcsEnvironmentDataFactory {

    private static final String TOOL_PREFIX = "aws-ecs";

    public static TornimoEnvironmentData getEnvironmentData(AwsEcsMetadataConfig awsEcsMetadataConfig,
                                                            AwsArnConfig awsArnConfig,
                                                            AwsEcsEndpoint awsEcsEndpoint) {
        return getEnvironmentData(awsEcsMetadataConfig, awsArnConfig, awsEcsEndpoint.query());
    }

    static TornimoEnvironmentData getEnvironmentData(AwsEcsMetadataConfig awsEcsMetadataConfig,
                                                     AwsArnConfig awsArnConfig,
                                                     String json) {
        return metadataToEnvironmentData(
                parseMetadata(json, awsEcsMetadataConfig),
                awsEcsMetadataConfig,
                awsArnConfig
        );
    }

    static TornimoEnvironmentData metadataToEnvironmentData(AwsEcsMetadata metadata,
                                                            AwsEcsMetadataConfig awsEcsMetadataConfig,
                                                            AwsArnConfig awsArnConfig) {
        StringBuilder builder = new StringBuilder();

        AwsArn awsArn = metadata.getAwsArn();
        AppendUtils.appendIfNotEmpty(builder, awsArn.getPartition(), awsArnConfig.isPartition(), awsArnConfig.getDefaultPartition(), "partition");
        AppendUtils.appendIfNotEmpty(builder, awsArn.getService(), awsArnConfig.isService(), awsArnConfig.getDefaultService(), "service");
        AppendUtils.appendIfNotEmpty(builder, awsArn.getRegion(), awsArnConfig.isRegion(), awsArnConfig.getDefaultRegion(), "region");
        AppendUtils.appendIfNotEmpty(builder, metadata.getAwsArn().getAccount(), awsArnConfig.isAccount(), awsArnConfig.getDefaultAccount(), "account");
        AppendUtils.appendIfNotEmpty(builder, awsArn.getResourceType(), awsArnConfig.isResourceType(), awsArnConfig.getDefaultResourceType(), "resourcetype");
        AppendUtils.appendIfNotEmpty(builder, awsArn.getResource(), awsArnConfig.isResource(), awsArnConfig.getDefaultResource(), "resource");
        AppendUtils.appendIfNotEmpty(builder, awsArn.getQualifier(), awsArnConfig.isQualifier(), awsArnConfig.getDefaultQualifier(), "qualifier");

        AppendUtils.appendIfNotEmpty(builder, metadata.getCluster(), awsEcsMetadataConfig.isCluster(), "", "cluster");
        AppendUtils.appendIfNotEmpty(builder, metadata.getRevision(), awsEcsMetadataConfig.isRevision(), "", "version");

        return new TornimoStaticEnvironmentData(AppendUtils.appendPrefix(TOOL_PREFIX, builder.toString()));
    }

    static AwsEcsMetadata parseMetadata(String json,
                                        AwsEcsMetadataConfig config) {
        return AwsEcsMetadata.formJson(json,
                config.isCluster(),
                config.isRevision(),
                config.isTaskArn()
                );
    }
}
