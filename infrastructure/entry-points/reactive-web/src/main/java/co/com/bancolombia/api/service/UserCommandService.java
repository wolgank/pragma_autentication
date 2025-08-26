package co.com.bancolombia.api.service;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserCommandService {

  private final UserUseCase useCase;

  @Transactional
  public Mono<User> registrar(User user) {
    return useCase.saveUser(user); 
  }
}