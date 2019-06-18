package io.tornimo.cloud.aws;

public class AwsEcsMetadataConfig {

    private final boolean parseCluster;
    private final boolean parseVersion;
    private final boolean parseContainerInstanceArn;

    AwsEcsMetadataConfig(boolean parseCluster,
                         boolean parseVersion,
                         boolean parseContainerInstanceArn) {
        this.parseCluster = parseCluster;
        this.parseVersion = parseVersion;
        this.parseContainerInstanceArn = parseContainerInstanceArn;
    }

    public boolean parseCluster() {
        return parseCluster;
    }

    public boolean parseVersion() {
        return parseVersion;
    }

    public boolean parseContainerInstanceArn() {
        return parseContainerInstanceArn;
    }
}
