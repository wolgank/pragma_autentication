package co.com.bancolombia.r2dbc;

import java.math.BigInteger;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.bancolombia.r2dbc.entity.UserEntity;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, BigInteger>, ReactiveQueryByExampleExecutor<UserEntity> {
}
