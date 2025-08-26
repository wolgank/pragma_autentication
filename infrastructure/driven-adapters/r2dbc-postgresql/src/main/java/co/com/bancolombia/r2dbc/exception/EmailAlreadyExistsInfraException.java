package co.com.bancolombia.r2dbc.exception;

public class EmailAlreadyExistsInfraException extends RuntimeException {
    public EmailAlreadyExistsInfraException(String email) {
        super("El correo ya está registrado: " + email);
    }
}