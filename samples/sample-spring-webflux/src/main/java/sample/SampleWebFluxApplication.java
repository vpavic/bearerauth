package sample;

import io.github.vpavic.bearerauth.AuthorizationContext;
import io.github.vpavic.bearerauth.BearerAuthenticationHandler;
import io.github.vpavic.bearerauth.BearerToken;
import io.github.vpavic.bearerauth.MapAuthorizationContextResolver;
import io.github.vpavic.bearerauth.spring.webflux.WebFluxBearerAuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SampleWebFluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleWebFluxApplication.class, args);
    }

    @Bean
    public WebFluxBearerAuthenticationFilter bearerAuthenticationFilter() {
        Map<BearerToken, AuthorizationContext> authorizationContexts = new HashMap<>();
        authorizationContexts.put(new BearerToken("valid"),
                new AuthorizationContext(Collections.emptySet(), Instant.MAX, Collections.emptyMap()));
        authorizationContexts.put(new BearerToken("expired"),
                new AuthorizationContext(Collections.emptySet(), Instant.MIN, Collections.emptyMap()));
        BearerAuthenticationHandler bearerAuthenticationHandler = BearerAuthenticationHandler.builder(
                new MapAuthorizationContextResolver(authorizationContexts)).build();
        return new WebFluxBearerAuthenticationFilter(bearerAuthenticationHandler);
    }

}
