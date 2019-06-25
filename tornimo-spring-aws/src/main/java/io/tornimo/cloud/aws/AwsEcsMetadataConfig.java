package io.tornimo.cloud.aws;

public class AwsEcsMetadataConfig {

    private final boolean cluster;
    private final boolean revision;
    private final boolean taskArn;

    public AwsEcsMetadataConfig(boolean cluster,
                                boolean revision,
                                boolean taskArn) {
        this.cluster = cluster;
        this.revision = revision;
        this.taskArn = taskArn;
    }

    public boolean isCluster() {
        return cluster;
    }

    public boolean isRevision() {
        return revision;
    }

    public boolean isTaskArn() {
        return taskArn;
    }
}
