package io.tornimo.tokens;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TornimoTokenDefinition {

    public static Pattern TOKEN_PATTERN = Pattern.compile("\\{(?<token>[\\w-_:]+)(\\|(?<default>[\\w-_]+))?(?<operator>!*)}");
    public static Pattern CONSTANT_PATTERN = Pattern.compile("[\\w-_]+");

    public static String DEFAULT_VALUE = "NA";

    private final String token;
    private final String defaultValue;
    private final boolean required;
    private final boolean constant;

    private TornimoTokenDefinition(String token, String defaultValue, boolean required, boolean constant) {
        this.token = token;
        this.defaultValue = defaultValue;
        this.required = required;
        this.constant = constant;
    }

    public String getToken() {
        return token;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isConstant() {
        return constant;
    }

    public static TornimoTokenDefinition fromString(String input) {
        Matcher matcher = TOKEN_PATTERN.matcher(input);
        if (matcher.matches()) {
            String token = matcher.group("token");

            String defaultValue = matcher.group("default");
            defaultValue = defaultValue == null ? DEFAULT_VALUE : defaultValue;

            boolean required = "!".equals(matcher.group("operator"));

            return new TornimoTokenDefinition(token, defaultValue, required, false);
        }
        matcher = CONSTANT_PATTERN.matcher(input);
        if (matcher.matches()) {
            return new TornimoTokenDefinition(input, "", true, true);
        }

        throw new IllegalArgumentException("invalid token '" + input + "'");
    }
}