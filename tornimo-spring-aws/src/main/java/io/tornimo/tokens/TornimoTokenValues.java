package io.tornimo.tokens;

import java.util.Map;
import java.util.Objects;

public class TornimoTokenValues {
    private final Map<String, String> tokenMap;

    public TornimoTokenValues(Map<String, String> tokenMap) {
        this.tokenMap = tokenMap;
    }

    public Map<String, String> getTokenMap() {
        return tokenMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TornimoTokenValues that = (TornimoTokenValues) o;
        return Objects.equals(tokenMap, that.tokenMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenMap);
    }
}
