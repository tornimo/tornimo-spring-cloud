package io.tornimo.cloud.aws;

import java.io.InputStream;
import java.net.URI;

public class AwsEcsV3Endpoint implements AwsEcsEndpoint {

    @Override
    public String query() {
        String service = System.getenv("ECS_CONTAINER_METADATA_URI") + "/task";
        try (InputStream stream = new URI(service).toURL().openStream()) {
            return IOUtils.toString(stream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke " + service, e);
        }
    }
}
