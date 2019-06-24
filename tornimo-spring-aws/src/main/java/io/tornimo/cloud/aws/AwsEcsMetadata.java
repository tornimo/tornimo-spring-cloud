package io.tornimo.cloud.aws;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AwsEcsMetadata {

    private static final Pattern CLUSTER_PATTERN = Pattern.compile(".*?\"Cluster\".*?:.*?\"(.*?)\".*?", Pattern.DOTALL);
    private static final Pattern VERSION_PATTERN = Pattern.compile(".*?\"Version\".*?:.*?\"(.*?)\".*?", Pattern.DOTALL);
    private static final Pattern CONTAINER_INSTANCE_ARN_PATTERN = Pattern.compile(".*?\"ContainerInstanceArn\".*?:.*?\"(.*?)\".*?", Pattern.DOTALL);

    private static final Pattern CLUSTER_PATTERN_V3 = Pattern.compile(".*?\"Cluster\".*?:.*?\"(.*?)\".*?", Pattern.DOTALL);
    private static final Pattern REVISION_PATTERN_V3 = Pattern.compile(".*?\"Revision\".*?:.*?\"(.*?)\".*?", Pattern.DOTALL);
    private static final Pattern TASK_ARN_PATTERN_V3 = Pattern.compile(".*?\"TaskARN\".*?:.*?\"(.*?)\".*?", Pattern.DOTALL);

    private final String cluster;
    private final String version;
    private final AwsArn awsArn;

    AwsEcsMetadata(String cluster,
                   String version,
                   AwsArn awsArn) {
        this.cluster = cluster;
        this.version = version;
        this.awsArn = awsArn;
    }

    public String getCluster() {
        return cluster;
    }

    public String getVersion() {
        return version;
    }

    public AwsArn getAwsArn() {
        return awsArn;
    }

    public static AwsEcsMetadata formJsonV3(String json,
                                            boolean parseCluster,
                                            boolean parseRevision,
                                            boolean parseTaskArn) {
        Matcher matcher;
        String version = "";
        String cluster = "";
        AwsArn awsArn = AwsArn.empty();

        if (parseCluster) {
            matcher = CLUSTER_PATTERN_V3.matcher(json);
            throwIfNotMatched(json, "Cluster", matcher);
            cluster = matcher.group(1).trim();
            throwIfMissing(json, "Cluster", cluster);
        }

        if (parseTaskArn) {
            matcher = TASK_ARN_PATTERN_V3.matcher(json);
            throwIfNotMatched(json, "TaskARN", matcher);
            String containerInstanceArn = matcher.group(1).trim();
            throwIfMissing(json, "TaskARN", containerInstanceArn);
            awsArn = AwsArn.fromString(containerInstanceArn);
        }

        if (parseRevision) {
            matcher = REVISION_PATTERN_V3.matcher(json);
            throwIfNotMatched(json, "Revision", matcher);
            version = matcher.group(1).trim();
            throwIfMissing(json, "Revision", version);
        }

        return new AwsEcsMetadata(cluster, version, awsArn);
    }

    public static AwsEcsMetadata formJson(String json,
                                          boolean parseCluster,
                                          boolean parseVersion,
                                          boolean parseContainerInstanceArn) {
        Matcher matcher;
        String version = "";
        String cluster = "";
        AwsArn awsArn = AwsArn.empty();

        if (parseCluster) {
            matcher = CLUSTER_PATTERN.matcher(json);
            throwIfNotMatched(json, "Cluster", matcher);
            cluster = matcher.group(1).trim();
            throwIfMissing(json, "Cluster", cluster);
        }

        if (parseContainerInstanceArn) {
            matcher = CONTAINER_INSTANCE_ARN_PATTERN.matcher(json);
            throwIfNotMatched(json, "ContainerInstanceArn", matcher);
            String containerInstanceArn = matcher.group(1).trim();
            throwIfMissing(json, "ContainerInstanceArn", containerInstanceArn);
            awsArn = AwsArn.fromString(containerInstanceArn);
        }

        if (parseVersion) {
            matcher = VERSION_PATTERN.matcher(json);
            throwIfNotMatched(json, "Version", matcher);
            version = matcher.group(1).trim();
            throwIfMissing(json, "Version", version);
        }

        return new AwsEcsMetadata(cluster, version, awsArn);
    }

    private static void throwIfNotMatched(String json, String jsonField, Matcher matcher) {
        if (!matcher.matches()) {
            throw new IllegalArgumentException("\"" + jsonField + "\" not found in \"" + json + "\"");
        }
    }

    private static void throwIfMissing(String json, String jsonField, String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("\"" + jsonField + "\" is empty in \"" + json + "\"");
        }
    }
}
