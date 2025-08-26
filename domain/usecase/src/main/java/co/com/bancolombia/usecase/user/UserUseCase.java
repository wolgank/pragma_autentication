package co.com.bancolombia.usecase.user;


import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    public Mono<User> saveUser(User user){
        return userRepository.saveUser(user);
    }
    public Flux<User> getAllUsers(){
        return userRepository.getAllUsers();
    }
    public Mono<User> getUserById(Long id){
        return userRepository.getUserById(id);
    }
    public Mono<Boolean> existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}