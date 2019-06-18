package io.tornimo.tokens;

import java.util.ArrayList;
import java.util.List;

public class TornimoPathTokens {

    private final List<TornimoTokenDefinition> tokens;

    public TornimoPathTokens(List<TornimoTokenDefinition> tokens) {
        this.tokens = tokens;
    }

    public List<TornimoTokenDefinition> getTokens() {
        return tokens;
    }

    public static TornimoPathTokens fromString(String tokens) {
        List<TornimoTokenDefinition> tokenList = new ArrayList<>();

        String[] split = tokens.split("\\.");
        if (split.length == 0) {
            throw new IllegalArgumentException("Tornimo path '" + tokens + "' is invalid ");
        }

        for (String item : split) {
            tokenList.add(TornimoTokenDefinition.fromString(item.trim()));
        }

        return new TornimoPathTokens(tokenList);
    }
}
