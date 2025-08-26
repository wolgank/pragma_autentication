package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import co.com.bancolombia.r2dbc.exception.EmailAlreadyExistsInfraException;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User/* change for domain model */,
    UserEntity/* change for adapter model */,
    Long,
    UserReactiveRepository
> implements UserRepository{

    private static final Logger log = LoggerFactory.getLogger(UserReactiveRepositoryAdapter.class);

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class/* change for domain model */));
    }

    @Override
    public Mono<User> saveUser(User user) {
        String normalized = user.getEmail() != null ? user.getEmail().trim().toLowerCase() : null;
        var toSave = user.toBuilder().email(normalized).build();

        return repository.existsEmail(normalized)
            .doOnNext(exists -> log.info("existsEmail({}) -> {}", normalized, exists))
            .flatMap(exists -> {
                if (Boolean.TRUE.equals(exists)) {
                    return Mono.error(new EmailAlreadyExistsInfraException(normalized));
                }
                return super.save(toSave)
                    .doOnSubscribe(s -> log.info("Insertando usuario email={}, name={} {}", normalized, toSave.getName(), toSave.getLastname()))
                    // ⚠️ SOLO mapear violación de UNIQUE (23505)
                    .onErrorMap(DuplicateKeyException.class,
                        ex -> new EmailAlreadyExistsInfraException(normalized))
                    // 👇 Loguea cualquier otro error para ver la causa real
                    .doOnError(t -> log.error("Error al guardar usuario en BD", t));
            });
    }

    @Override
    public Flux<User> getAllUsers() {
        return super.findAll();
    }

    @Override
    public Mono<User> getUserById(Long id) {
        return super.findById(id);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        String normalized = email != null ? email.trim().toLowerCase() : null;
        return repository.existsEmail(normalized);
    }

}
