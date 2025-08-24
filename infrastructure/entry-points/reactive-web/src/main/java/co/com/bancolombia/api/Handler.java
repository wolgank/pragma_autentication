package co.com.bancolombia.api;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.user.UserUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    
    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(User.class)
                .flatMap(userUseCase::saveUser)
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser));
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
                            .body(userUseCase.getAllUsers(), User.class);
    }

    public Mono<ServerResponse> listenGetUserById(ServerRequest serverRequest) {
        java.math.BigInteger userId = new java.math.BigInteger(serverRequest.pathVariable("id"));
        return userUseCase.getUserById(userId)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
