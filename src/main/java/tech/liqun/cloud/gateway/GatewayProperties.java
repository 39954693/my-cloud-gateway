package tech.liqun.cloud.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author DHC
 **/
@ConfigurationProperties("spring.cloud.gateway")
public class GatewayProperties {

    /**
     * Map of route names to properties.
     */
    private Map<String, Route> routes = new LinkedHashMap<>();

    public Map<String, Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, Route> routes) {
        this.routes = routes;
    }

    public static class Route {
        private String id;
        private String path;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
