package co.com.bancolombia.r2dbc;



import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.bancolombia.r2dbc.entity.UserEntity;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {
    @Query("SELECT EXISTS (SELECT 1 FROM users WHERE LOWER(email) = LOWER($1))")
    Mono<Boolean> existsEmail(String email);
    Mono<UserEntity> findByEmailIgnoreCase(String email);
}
