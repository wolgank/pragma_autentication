package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.bancolombia.api.config.UserPath;
import lombok.RequiredArgsConstructor;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {
    private final Handler userHandler;
    private final UserPath userPath;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(userPath.getUsers()), userHandler::listenSaveUser)
                .andRoute(GET(userPath.getUsers()), userHandler::listenGetAllUsers)
                .and(route(GET(userPath.getUsersById()), userHandler::listenGetUserById));
    }
}
