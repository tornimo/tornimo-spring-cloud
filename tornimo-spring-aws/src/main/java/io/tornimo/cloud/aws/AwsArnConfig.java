package io.tornimo.cloud.aws;

public class AwsArnConfig {

    private final boolean partition;
    private final boolean service;
    private final boolean region;
    private final boolean account;
    private final boolean resourceType;
    private final boolean resource;
    private final boolean qualifier;

    private final String defaultPartition;
    private final String defaultService;
    private final String defaultRegion;
    private final String defaultAccount;
    private final String defaultResourceType;
    private final String defaultResource;
    private final String defaultQualifier;

    public AwsArnConfig(boolean partition,
                        boolean service,
                        boolean region,
                        boolean account,
                        boolean resourceType,
                        boolean resource,
                        boolean qualifier,
                        String defaultPartition,
                        String defaultService,
                        String defaultRegion,
                        String defaultAccount,
                        String defaultResourceType,
                        String defaultResource,
                        String defaultQualifier) {
        this.partition = partition;
        this.service = service;
        this.region = region;
        this.account = account;
        this.resourceType = resourceType;
        this.resource = resource;
        this.qualifier = qualifier;
        this.defaultPartition = defaultPartition;
        this.defaultService = defaultService;
        this.defaultRegion = defaultRegion;
        this.defaultAccount = defaultAccount;
        this.defaultResourceType = defaultResourceType;
        this.defaultResource = defaultResource;
        this.defaultQualifier = defaultQualifier;
    }

    public boolean isPartition() {
        return partition;
    }

    public boolean isService() {
        return service;
    }

    public boolean isRegion() {
        return region;
    }

    public boolean isAccount() {
        return account;
    }

    public boolean isResourceType() {
        return resourceType;
    }

    public boolean isResource() {
        return resource;
    }

    public boolean isQualifier() {
        return qualifier;
    }

    public String getDefaultPartition() {
        return defaultPartition;
    }

    public String getDefaultService() {
        return defaultService;
    }

    public String getDefaultRegion() {
        return defaultRegion;
    }

    public String getDefaultAccount() {
        return defaultAccount;
    }

    public String getDefaultResourceType() {
        return defaultResourceType;
    }

    public String getDefaultResource() {
        return defaultResource;
    }

    public String getDefaultQualifier() {
        return defaultQualifier;
    }
}
