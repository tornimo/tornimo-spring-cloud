package io.tornimo.cloud.aws;

import io.tornimo.TornimoEnvironmentData;
import io.tornimo.TornimoStaticEnvironmentData;

import java.io.InputStream;
import java.net.URI;

public class AwsEcsV3EnvironmentDataFactory {

    private static final String TOOL_PREFIX = "aws-ecs";

    public static TornimoEnvironmentData getEnvironmentData(AwsEcsMetadataConfig awsEcsMetadataConfig,
                                                            AwsArnConfig awsArnConfig) {
        String service = System.getenv("ECS_CONTAINER_METADATA_URI") + "/task";
        try (InputStream stream = new URI(service).toURL().openStream()) {
            return getEnvironmentData(awsEcsMetadataConfig, awsArnConfig, IOUtils.toString(stream));
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke " + service, e);
        }
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
        appendIfNotEmpty(builder, awsArn.getPartition(), awsArnConfig.isPartition(), awsArnConfig.getDefaultPartition(), "partition");
        appendIfNotEmpty(builder, awsArn.getService(), awsArnConfig.isService(), awsArnConfig.getDefaultService(), "service");
        appendIfNotEmpty(builder, awsArn.getRegion(), awsArnConfig.isRegion(), awsArnConfig.getDefaultRegion(), "region");
        appendIfNotEmpty(builder, metadata.getAwsArn().getAccount(), awsArnConfig.isAccount(), awsArnConfig.getDefaultAccount(), "account");
        appendIfNotEmpty(builder, awsArn.getResourceType(), awsArnConfig.isResourceType(), awsArnConfig.getDefaultResourceType(), "resourcetype");
        appendIfNotEmpty(builder, awsArn.getResource(), awsArnConfig.isResource(), awsArnConfig.getDefaultResource(), "resource");
        appendIfNotEmpty(builder, awsArn.getQualifier(), awsArnConfig.isQualifier(), awsArnConfig.getDefaultQualifier(), "qualifier");

        appendIfNotEmpty(builder, metadata.getCluster(), awsEcsMetadataConfig.isCluster(), "", "cluster");
        appendIfNotEmpty(builder, metadata.getRevision(), awsEcsMetadataConfig.isRevision(), "", "version");

        String result = builder.toString();

        return new TornimoStaticEnvironmentData(TOOL_PREFIX + "." + result.substring(0, result.length() - 1));
    }

    static AwsEcsMetadata parseMetadata(String json,
                                        AwsEcsMetadataConfig config) {
        return AwsEcsMetadata.formJson(json,
                config.isCluster(),
                config.isRevision(),
                config.isTaskArn());
    }

    static void appendIfNotEmpty(StringBuilder builder,
                                 String value,
                                 boolean enabled,
                                 String defaultValue,
                                 String fieldName) {
        if (enabled) {
            if (!value.trim().isEmpty()) {
                builder.append(value.replace(".", "_")).append(".");
            } else if (!defaultValue.isEmpty()) {
                builder.append(defaultValue.replace(".", "_")).append(".");
            } else {
                throw new IllegalArgumentException(fieldName + " is missing and no default value is provided");
            }
        }
    }
}
