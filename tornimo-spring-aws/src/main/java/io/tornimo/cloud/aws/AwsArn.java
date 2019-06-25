package io.tornimo.cloud.aws;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AwsArn {

    private static final Pattern ARN_PATTERN =
            Pattern.compile("arn:(?<partition>.*?):(?<service>.*?):(?<region>.*?):(?<account>.*?):(?<resource>.*?)");

    /**
     * https://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html
     */
    private static final Pattern RESOURCE_PATTERN_A = Pattern.compile("(?<resourcetype>.*?)[:/](?<resource>.*?)[:/](?<qualifier>.*?)");
    private static final Pattern RESOURCE_PATTERN_B = Pattern.compile("(?<resourcetype>.*?)[:/](?<resource>.*?)");
    private static final Pattern RESOURCE_PATTERN_C = Pattern.compile("(?<resource>.*?)");
    private static final Pattern[] RESOURCE_PATTERN = {RESOURCE_PATTERN_A, RESOURCE_PATTERN_B, RESOURCE_PATTERN_C};

    private final String partition;
    private final String service;
    private final String region;
    private final String account;
    private final String resourceType;
    private final String resource;
    private final String qualifier;

    public static AwsArn fromString(String arn) {
        String resourceType = "";
        String resource = "";
        String qualifier = "";

        Matcher matcher = ARN_PATTERN.matcher(arn);
        if (matcher.matches()) {
            for (Pattern candidate : RESOURCE_PATTERN) {
                Matcher resourceMatcher = candidate.matcher(matcher.group("resource"));
                if (resourceMatcher.matches()) {
                    resourceType = getGroupOrEmpty(resourceMatcher, "resourcetype");
                    resource = getGroupOrEmpty(resourceMatcher, "resource");
                    qualifier = getGroupOrEmpty(resourceMatcher, "qualifier");
                    break;
                }
            }
            String partition = matcher.group("partition");
            String service = matcher.group("service");
            String region = matcher.group("region");
            String account = matcher.group("account");

            return new AwsArn(partition, service, region, account, resourceType, resource, qualifier);
        }

        throw new IllegalArgumentException("arn \"" + arn + "\" could not be parsed.");
    }

    public static AwsArn empty() {
        return new AwsArn("", "", "", "", "", "", "");
    }

    private AwsArn(String partition,
                   String service,
                   String region,
                   String account,
                   String resourceType,
                   String resource,
                   String qualifier) {
        this.partition = partition;
        this.service = service;
        this.region = region;
        this.account = account;
        this.resourceType = resourceType;
        this.resource = resource;
        this.qualifier = qualifier;
    }

    public String getPartition() {
        return partition;
    }

    public String getService() {
        return service;
    }

    public String getRegion() {
        return region;
    }

    public String getAccount() {
        return account;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResource() {
        return resource;
    }

    public String getQualifier() {
        return qualifier;
    }

    public boolean isEmpty() {
        return partition.isEmpty() &&
                service.isEmpty() &&
                region.isEmpty() &&
                account.isEmpty() &&
                resourceType.isEmpty() &&
                resource.isEmpty() &&
                qualifier.isEmpty();
    }

    private static String getGroupOrEmpty(Matcher matcher,
                                          String name) {
        try {
            return matcher.group(name);
        } catch (IllegalArgumentException e) {
            return "";
        }
    }
}
