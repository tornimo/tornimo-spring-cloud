package io.tornimo.cloud.aws;

import io.tornimo.cloud.InstanceId;
import io.tornimo.cloud.StaticInstanceId;

import java.io.InputStream;
import java.net.URI;

public class AwsEcsInstanceIdFactory {

    public static InstanceId getInstanceId(AwsEcsMetadataConfig awsEcsMetadataConfig, AwsArnConfig awsArnConfig) {
        String service = "http://localhost:51678/v1/metadata";
        try (InputStream stream = new URI(service).toURL().openStream()) {
            return getInstanceId(awsEcsMetadataConfig, awsArnConfig, IOUtils.toString(stream));
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke " + service, e);
        }
    }

    public static InstanceId getInstanceId(AwsEcsMetadataConfig awsEcsMetadataConfig, AwsArnConfig awsArnConfig, boolean localOnFailure) {
        if (localOnFailure) {
            try {
                return getInstanceId(awsEcsMetadataConfig, awsArnConfig);
            } catch (Exception e) {
                return metadataToInstanceId(
                        new AwsEcsMetadata(
                                "local_cluster",
                                "local_version",
                                AwsArn.fromString("arn:partition:service:region:account:resource")
                        ),
                        awsEcsMetadataConfig,
                        awsArnConfig
                );
            }
        } else {
            return getInstanceId(awsEcsMetadataConfig, awsArnConfig);
        }
    }

    static InstanceId getInstanceId(AwsEcsMetadataConfig awsEcsMetadataConfig, AwsArnConfig awsArnConfig, String json) {
        return metadataToInstanceId(parseMetadata(json, awsEcsMetadataConfig), awsEcsMetadataConfig, awsArnConfig);
    }

    static InstanceId metadataToInstanceId(AwsEcsMetadata metadata, AwsEcsMetadataConfig awsEcsMetadataConfig, AwsArnConfig awsArnConfig) {
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
        appendIfNotEmpty(builder, metadata.getVersion(), awsEcsMetadataConfig.isVersion(), "", "version");

        String result = builder.toString();

        return new StaticInstanceId(result.substring(0, result.length() - 1));
    }

    static AwsEcsMetadata parseMetadata(String json, AwsEcsMetadataConfig config) {
        return AwsEcsMetadata.formJson(json,
                config.isCluster(),
                config.isVersion(),
                config.isContainerInstanceArn());
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

