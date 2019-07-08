package io.tornimo.cloud.aws;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AwsEcsMetadata {

    private static final Pattern CLUSTER_PATTERN_V3 = Pattern.compile(".*?\"Cluster\".*?:.*?\"(.*?)\".*?", Pattern.DOTALL);
    private static final Pattern REVISION_PATTERN_V3 = Pattern.compile(".*?\"Revision\".*?:.*?\"(.*?)\".*?", Pattern.DOTALL);
    private static final Pattern TASK_ARN_PATTERN_V3 = Pattern.compile(".*?\"TaskARN\".*?:.*?\"(.*?)\".*?", Pattern.DOTALL);

    private final String cluster;
    private final String revision;
    private final AwsArn awsArn;

    public static AwsEcsMetadata fromJson(String json,
                                          boolean parseCluster,
                                          boolean parseRevision,
                                          boolean parseTaskArn) {
        Matcher matcher;
        String revision = "";
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
            revision = matcher.group(1).trim();
            throwIfMissing(json, "Revision", revision);
        }

        return new AwsEcsMetadata(cluster, revision, awsArn);
    }

    AwsEcsMetadata(String cluster,
                   String revision,
                   AwsArn awsArn) {
        this.cluster = cluster;
        this.revision = revision;
        this.awsArn = awsArn;
    }

    public String getCluster() {
        return cluster;
    }

    public String getRevision() {
        return revision;
    }

    public AwsArn getAwsArn() {
        return awsArn;
    }

    private static void throwIfNotMatched(String json,
                                          String jsonField,
                                          Matcher matcher) {
        if (!matcher.matches()) {
            throw new IllegalArgumentException("\"" + jsonField + "\" not found in \"" + json + "\"");
        }
    }

    private static void throwIfMissing(String json,
                                       String jsonField,
                                       String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("\"" + jsonField + "\" is empty in \"" + json + "\"");
        }
    }
}
