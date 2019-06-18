package io.tornimo.cloud.spring.autoconfigure;

import java.util.Iterator;

/**
 * Class representing a path for metrics in Tornimo.io
 * <p>
 * token={[region|account|resourcetype|resource|qualifier]}
 * aws.{service}.[{token[|default-value]}]+
 * All of the tokens come from <a href=https://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html>aws:arn</a>
 * <p>
 * default is set to {@link TornimoPathProperties#DEFAULT_TOKENS}
 * default value is {@link TornimoPathProperties#DEFAULT_VALUE}
 * <p>
 * example custom path "myapp.{region|local}.{resource}.{qualifier|not-found}"
 * <p>
 * {@link TornimoPathProperties#PREFIX} will be always added to the metrics path
 */
public class TornimoPathProperties {

    private final static String DEFAULT_TOKENS = "{region}.{resourcetype}.{resource}.{qualifier}";
    public final static String DEFAULT_VALUE = "NA";
    private final static String PREFIX = "aws.service.";

    private final String pattern;

    public TornimoPathProperties(String pattern) {
        this.pattern = pattern;
    }

    public Iterable<PathEntry> entries() {
        String[] split;
        if (pattern == null || pattern.trim().isEmpty()) {
            split = (PREFIX + DEFAULT_TOKENS).split("\\.");
        } else {
            split = (PREFIX + pattern).split("\\.");
        }
        for (String entry : split) {
            System.out.println(parseEntry(entry.trim()));
        }

        return null;
    }

    private PathEntry parseEntry(String entry) {
        if (entry.matches("\\{.+}")) {
            entry = entry.substring(0, entry.length() - 1);
            String[] typeAndDefault = entry.split("\\|");
            if (typeAndDefault.length == 0) {
                throw new IllegalArgumentException("No token provided in path.");
            } else if (typeAndDefault.length == 1) {
                if (typeAndDefault[0].trim().isEmpty()) {
                    throw new IllegalArgumentException("token is empty");
                }
                return new PathEntry(typeAndDefault[0].trim().substring(1, typeAndDefault[0].length() - 1), DEFAULT_VALUE, false);
            } else if (typeAndDefault.length == 2) {
                if (typeAndDefault[0].trim().isEmpty() || typeAndDefault[1].trim().isEmpty()) {
                    throw new IllegalArgumentException("token or default value is empty");
                }

                return new PathEntry(typeAndDefault[0], typeAndDefault[1], false);
            } else {
                throw new IllegalArgumentException("Too many default values provided");
            }
        } else {
            if (entry.contains("|")) {
                throw new IllegalArgumentException("character '|' not allowed in constant");
            }
            return new PathEntry(entry, "", true);
        }
    }

    public static void main(String[] args) {
        new TornimoPathProperties("vendor.service.{region|not-found}.{resource}").entries();
        new TornimoPathProperties("").entries();
    }

    class PathEntry {

        private final String token, defaultValue;
        private final boolean constant;

        PathEntry(String token, String defaultValue, boolean constant) {
            this.token = token;
            this.defaultValue = defaultValue;
            this.constant = constant;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("PathEntry{");
            sb.append("token='").append(token).append('\'');
            sb.append(", defaultValue='").append(defaultValue).append('\'');
            sb.append(", constant=").append(constant);
            sb.append('}');
            return sb.toString();
        }
    }

    class PathEntryIterator implements Iterator<PathEntry> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public PathEntry next() {
            return null;
        }
    }
}
