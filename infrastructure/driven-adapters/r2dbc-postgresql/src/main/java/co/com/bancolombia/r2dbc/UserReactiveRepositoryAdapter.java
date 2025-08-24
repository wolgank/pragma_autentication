package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User/* change for domain model */,
    UserEntity/* change for adapter model */,
    BigInteger,
    UserReactiveRepository
> implements UserRepository{
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
        return super.save(user);
    }

    @Override
    public Flux<User> getAllUsers() {
        return super.findAll();
    }

    @Override
    public Mono<User> getUserById(BigInteger id) {
        return super.findById(id);
    }

}
