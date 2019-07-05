package io.tornimo.cloud.aws;

import java.io.InputStream;
import java.net.URI;

public class AwsEcsV2Endpoint implements AwsEndpoint {

    @Override
    public String query() {
        String service = "169.254.170.2/v2/metadata/";
        try (InputStream stream = new URI(service).toURL().openStream()) {
            return IOUtils.toString(stream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke " + service, e);
        }
    }
}
