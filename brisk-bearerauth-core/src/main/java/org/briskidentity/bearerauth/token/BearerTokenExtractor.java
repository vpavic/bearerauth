package org.briskidentity.bearerauth.token;

import org.briskidentity.bearerauth.http.HttpExchange;

import java.util.function.Function;

/**
 * A specialization of {@link Function} used for extracting bearer token from HTTP request.
 */
@FunctionalInterface
public interface BearerTokenExtractor extends Function<HttpExchange, BearerToken> {

    /**
     * @return the authorization header bearer token extractor
     */
    static BearerTokenExtractor authorizationHeader() {
        return AuthorizationHeaderBearerTokenExtractor.INSTANCE;
    }

}
