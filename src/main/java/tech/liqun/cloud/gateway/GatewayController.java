package tech.liqun.cloud.gateway;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
public class GatewayController {
    private final WebClient webClient;

    public GatewayController(WebClient webClient) {
        this.webClient = webClient;
    }

    //TODO: plugin to request mappings
    @GetMapping("/")
    public Mono<Void> home(ServerWebExchange exchange) {
        return this.webClient.get().uri((String) Objects.requireNonNull(exchange.getAttribute("requestUrl")))
                .headers(httpHeaders -> httpHeaders.addAll(exchange.getRequest().getHeaders()))
                .exchange().flatMap(clientResponse -> {
                    ServerHttpResponse response = exchange.getResponse();
                    response.getHeaders().putAll(clientResponse.headers().asHttpHeaders());
                    response.setStatusCode(clientResponse.statusCode());
                    return response.writeWith(clientResponse.body((inputMessage, context) -> inputMessage.getBody()));
                });
    }


}
