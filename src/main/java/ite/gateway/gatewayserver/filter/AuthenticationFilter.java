package ite.gateway.gatewayserver.filter;
import ite.gateway.gatewayserver.exception.RoleExceptionHandler;
import ite.gateway.gatewayserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    //    @Autowired
//    private RestTemplate template;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                List<String> role = jwtUtil.extractRoles(authHeader);
                String requestedRoute = exchange.getRequest().getPath().value();
                boolean isSecured = isRouteSecured(requestedRoute, config.getSecuredRoutes());
                   if (!role.contains(config.getRequiredRole()) && isSecured ) {
                       throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access required");
                   }
                try {
//                    //REST call to AUTH service
//                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);
                    jwtUtil.validateToken(authHeader);


                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }
    private boolean isRouteSecured(String requestedRoute, List<String> securedRoutes) {
        for (String securedRoute : securedRoutes) {
            // Check if the requested route starts with the secured route
            if (requestedRoute.startsWith(securedRoute)) {
                return true;
            }
        }
        return false;
    }

    public static class Config {
        private List<String> securedRoutes;
        private String requiredRole;

        public List<String> getSecuredRoutes() {
            return securedRoutes;
        }

        public void setSecuredRoutes(List<String> securedRoutes) {
            this.securedRoutes = securedRoutes;
        }

        public String getRequiredRole() {
            return requiredRole;
        }

        public void setRequiredRole(String requiredRole) {
            this.requiredRole = requiredRole;
        }
    }
    }
