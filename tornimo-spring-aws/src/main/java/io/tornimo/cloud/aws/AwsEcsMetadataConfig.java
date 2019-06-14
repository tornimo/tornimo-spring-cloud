package io.tornimo.cloud.aws;

class AwsEcsMetadataConfig {

    private final boolean cluster;
    private final boolean version;
    private final boolean containerInstanceArn;

    AwsEcsMetadataConfig(boolean cluster,
                         boolean version,
                         boolean containerInstanceArn) {
        this.cluster = cluster;
        this.version = version;
        this.containerInstanceArn = containerInstanceArn;
    }

    public boolean isCluster() {
        return cluster;
    }

    public boolean isVersion() {
        return version;
    }

    public boolean isContainerInstanceArn() {
        return containerInstanceArn;
    }
}
