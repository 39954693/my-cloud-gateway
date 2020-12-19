package tech.liqun.cloud.gateway.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tech.liqun.cloud.gateway.GatewayProperties;

import java.net.URI;

/**
 * @author DHC
 **/
public class FindRouterFilter implements WebFilter {

    private static final Log log = LogFactory.getLog(FindRouterFilter.class);

    private final GatewayProperties properties;
    private final AntPathMatcher matcher;

    public FindRouterFilter(GatewayProperties properties) {
        this.properties = properties;
        this.matcher = new AntPathMatcher();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("FindRouteFilter start");
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String path = uri.getPath();
        for (GatewayProperties.Route route : this.properties.getRoutes().values()) {
            if (this.matcher.match(route.getPath(), path)) {
                URI requestUri = UriComponentsBuilder.fromHttpRequest(request)
                        .host(route.getHost())
                        .port(route.getPort())
                        .build(true)
                        .toUri();
                exchange.getAttributes().put("requestUri", requestUri);
                return chain.filter(exchange);
            }
        }
        return chain.filter(exchange);

    }
}
