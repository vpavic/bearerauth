package io.github.vpavic.bearerauth.servlet;

import io.github.vpavic.bearerauth.BearerAuthenticationHandler;
import io.github.vpavic.bearerauth.BearerTokenException;
import io.github.vpavic.bearerauth.HttpExchange;
import io.github.vpavic.bearerauth.WwwAuthenticateBuilder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ServletBearerAuthenticationFilter implements Filter {

    private final BearerAuthenticationHandler bearerAuthenticationHandler;

    public ServletBearerAuthenticationFilter(BearerAuthenticationHandler bearerAuthenticationHandler) {
        Objects.requireNonNull(bearerAuthenticationHandler, "bearerAuthenticationHandler must not be null");
        this.bearerAuthenticationHandler = bearerAuthenticationHandler;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("ServletBearerAuthenticationFilter only supports HTTP requests");
        }
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        try {
            this.bearerAuthenticationHandler.handle(new ServletHttpExchange(req)).toCompletableFuture().get();
            chain.doFilter(request, response);
        }
        catch (ExecutionException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof BearerTokenException) {
                BearerTokenException bearerTokenException = (BearerTokenException) cause;
                String wwwAuthenticate = WwwAuthenticateBuilder.from(bearerTokenException).build();
                res.addHeader("WWW-Authenticate", wwwAuthenticate);
                res.sendError(bearerTokenException.getStatus());
            }
            else {
                throw new ServletException(ex);
            }
        }
        catch (InterruptedException ex) {
            throw new ServletException(ex);
        }
    }

    private static class ServletHttpExchange implements HttpExchange {

        private final HttpServletRequest httpServletRequest;

        private ServletHttpExchange(HttpServletRequest httpServletRequest) {
            this.httpServletRequest = httpServletRequest;
        }

        @Override
        public String getRequestHeader(String headerName) {
            return this.httpServletRequest.getHeader(headerName);
        }

        @Override
        public void setAttribute(String attributeName, Object attributeValue) {
            httpServletRequest.setAttribute(attributeName, attributeValue);
        }

    }

}