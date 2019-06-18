package io.tornimo.tokens;

import java.util.HashMap;
import java.util.Map;

public class TornimoRequestedTokens {

    private final Map<String, TornimoTokenDefinition> tokenMap;

    public TornimoRequestedTokens(Map<String, TornimoTokenDefinition> tokenMap) {
        this.tokenMap = tokenMap;
    }

    public boolean isTokenRequested(String token) {
        return tokenMap.containsKey(token);
    }

    public boolean isTokenRequired(String token) {
        return tokenMap.get(token).isRequired();
    }

    public TornimoTokenDefinition getTokenDefinition(String token) {
        return tokenMap.get(token);
    }

    public String tokenDefaultValue(String token) {
        TornimoTokenDefinition definition = tokenMap.get(token);
        if (definition == null) {
            return "";
        }
        return definition.getDefaultValue();
    }

    public boolean groupRequestedAsRequired(String group) {
        return tokenMap.entrySet().stream()
                .anyMatch(entry -> entry.getKey().startsWith(group + ":"));
    }

    public static TornimoRequestedTokens fromTornimoPathTokens(TornimoPathTokens tokens) {
        Map<String, TornimoTokenDefinition> tokenMap = new HashMap<>();
        for (TornimoTokenDefinition token : tokens.getTokens()) {
            if (!token.isConstant()) {
                tokenMap.put(token.getToken(), token);
            }
        }
        return new TornimoRequestedTokens(tokenMap);
    }
}
