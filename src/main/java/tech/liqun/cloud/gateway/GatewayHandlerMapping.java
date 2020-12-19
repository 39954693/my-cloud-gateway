package tech.liqun.cloud.gateway;

import org.springframework.beans.BeansException;
import org.springframework.http.server.PathContainer;
import org.springframework.web.reactive.handler.AbstractUrlHandlerMapping;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

/**
 * @author DHC
 **/
public class GatewayHandlerMapping extends AbstractUrlHandlerMapping {

    private GatewayProperties properties;
    private GatewayWebHandler gatewayWebHandler;

    public GatewayHandlerMapping(GatewayProperties properties, GatewayWebHandler gatewayWebHandler) {
        this.properties = properties;
        this.gatewayWebHandler = gatewayWebHandler;
    }

    @Override
    protected Object lookupHandler(PathContainer lookupPath, ServerWebExchange exchange) throws Exception {
        return super.lookupHandler(lookupPath, exchange);
    }

    @Override
    protected void initApplicationContext() throws BeansException {
        super.initApplicationContext();
        registerHandlers(this.properties.getRoutes());
    }

    protected void registerHandlers(Map<String, GatewayProperties.Route> routes){
        for (GatewayProperties.Route route : routes.values()) {
            registerHandler(route.getPath(), this.gatewayWebHandler);
        }
    }
}
