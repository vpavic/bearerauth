package org.briskidentity.bearerauth.token;

import org.briskidentity.bearerauth.http.HttpExchange;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link BearerTokenExtractor} implementation that extracts token from {@code Authorization} HTTP request header.
 */
class AuthorizationHeaderBearerTokenExtractor implements BearerTokenExtractor {

    static final AuthorizationHeaderBearerTokenExtractor INSTANCE = new AuthorizationHeaderBearerTokenExtractor();

    private static final Pattern authorizationHeaderPattern = Pattern.compile("^Bearer (?<token>[A-Za-z0-9\\-._~+/]+=*)");

    private AuthorizationHeaderBearerTokenExtractor() {
    }

    @Override
    public BearerToken apply(HttpExchange httpExchange) {
        Objects.requireNonNull(httpExchange, "httpExchange must not be null");
        String authorizationHeader = httpExchange.getRequestHeader("Authorization");
        if (authorizationHeader == null) {
            return null;
        }
        Matcher matcher = authorizationHeaderPattern.matcher(authorizationHeader);
        if (!matcher.matches()) {
            return null;
        }
        return new BearerToken(matcher.group("token"));
    }

}
