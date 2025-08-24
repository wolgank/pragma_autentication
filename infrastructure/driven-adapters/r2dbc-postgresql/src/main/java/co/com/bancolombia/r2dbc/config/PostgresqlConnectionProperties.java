package co.com.bancolombia.r2dbc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// TODO: Load properties from the application.yaml file or from secrets manager
// import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapters.r2dbc")
public record PostgresqlConnectionProperties(
        String host,
        Integer port,
        String database,
        String schema,
        String username,
        String password) {
}
