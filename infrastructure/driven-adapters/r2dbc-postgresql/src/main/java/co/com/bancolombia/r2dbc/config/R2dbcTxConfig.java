package co.com.bancolombia.r2dbc.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;

@Configuration
public class R2dbcTxConfig {
  @Bean
  public R2dbcTransactionManager transactionManager(ConnectionFactory cf) {
    return new R2dbcTransactionManager(cf);
  }
}