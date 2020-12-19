package tech.liqun.cloud.gateway;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;

/**
 * @author DHC
 **/
@SuppressWarnings("unused")
public class GatewayWebHandler implements WebHandler {

    private final GatewayProperties properties;
    private final WebClient webClient;

    public GatewayWebHandler(GatewayProperties properties, WebClient webClient) {
        this.properties = properties;
        this.webClient = webClient;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        return this.webClient.get().uri((URI) Objects.requireNonNull(exchange.getAttribute("requestUri")))
                .headers(httpHeaders -> httpHeaders.addAll(exchange.getRequest().getHeaders()))
                .exchange().flatMap(clientResponse -> {
                    ServerHttpResponse response = exchange.getResponse();
                    response.getHeaders().putAll(clientResponse.headers().asHttpHeaders());
                    response.setStatusCode(clientResponse.statusCode());
                    Flux<DataBuffer> body = clientResponse.body((inputMessage, context) -> inputMessage.getBody());
                    return response.writeWith(body);
                });
    }
}
