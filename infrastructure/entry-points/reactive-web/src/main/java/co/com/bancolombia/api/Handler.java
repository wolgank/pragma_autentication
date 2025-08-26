package co.com.bancolombia.api;

import co.com.bancolombia.api.config.UserPath;
import co.com.bancolombia.api.dto.RegistrarUsuarioRequest;
import co.com.bancolombia.api.mapper.UserMapper;
import co.com.bancolombia.api.service.UserCommandService;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.user.UserUseCase;
import org.springframework.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Validated
public class Handler {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    private final UserUseCase userUseCase;           // para GETs
    private final UserCommandService commandService; // servicio con @Transactional
    private final Validator validator;               // Bean Validation
    private final UserPath userPath;                  // ← inyectamos rutas

    public Mono<ServerResponse> listenSaveUser(ServerRequest req) {
        return req.bodyToMono(RegistrarUsuarioRequest.class)
                .doOnNext(b -> log.info("POST {} email={}", userPath.getUsers(), b.correo_electronico()))
                .flatMap(this::validate)
                .map(UserMapper::toDomain)
                .flatMap(commandService::registrar) // corre dentro de @Transactional
                .doOnSuccess(u -> log.info("Usuario registrado id={} email={}", u.getId(), u.getEmail()))
                .flatMap(saved -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(saved))
                // Conflicto por unicidad (índice único o adapter onErrorMap)
                .onErrorResume(co.com.bancolombia.r2dbc.exception.EmailAlreadyExistsInfraException.class, e ->
                    ServerResponse.status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(java.util.Map.of("code","EMAIL_DUPLICADO","message", e.getMessage())))
                .onErrorResume(org.springframework.dao.DuplicateKeyException.class, e ->
                    ServerResponse.status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(java.util.Map.of("code","EMAIL_DUPLICADO","message","Correo ya registrado")))
                .onErrorResume(org.springframework.dao.DataIntegrityViolationException.class, e ->
                    ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(java.util.Map.of("code","DATA_INTEGRITY","message","Datos inválidos o faltantes")));
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest req) {
        log.info("GET {}", req.path());
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userUseCase.getAllUsers(), User.class);
    }

    public Mono<ServerResponse> listenGetUserById(ServerRequest req) {
        log.info("GET {}", req.path());
        final Long userId;
        try {
            userId = Long.valueOf(req.pathVariable("id"));
        } catch (NumberFormatException ex) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "id inválido"));
        }
        return userUseCase.getUserById(userId)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    // ---- validación manual para functional endpoints ----
    private <T> Mono<T> validate(T body) {
        var errors = new BeanPropertyBindingResult(body, body.getClass().getName());
        validator.validate(body, errors);
        if (errors.hasErrors()) {
            String msg = errors.getAllErrors().get(0).getDefaultMessage();
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, msg));
        }
        return Mono.just(body);
    }
}
