package io.tornimo.cloud.aws;

public class AppendUtils {

    static void appendIfNotEmpty(StringBuilder builder,
                                 String value,
                                 boolean enabled,
                                 String defaultValue,
                                 String fieldName) {
        if (enabled) {
            if (!value.trim().isEmpty()) {
                builder.append(value.replace(".", "_")).append(".");
            } else if (!defaultValue.isEmpty()) {
                builder.append(defaultValue.replace(".", "_")).append(".");
            } else {
                throw new IllegalArgumentException(fieldName + " is missing and no default value is provided");
            }
        }
    }

    static void appendIfNotEmpty(StringBuilder builder,
                                 String value,
                                 boolean enabled,
                                 String fieldName) {
        if (enabled) {
            if (!value.trim().isEmpty()) {
                builder.append(value.replace(".", "_")).append(".");
            } else {
                throw new IllegalArgumentException(fieldName + " is missing and no default value is provided");
            }
        }
    }

    static String appendPrefix(String prefix, String string) {
        if (string.isEmpty()) {
            return prefix;
        }

        return prefix + "." + string.substring(0, string.length() - 1);
    }
}
