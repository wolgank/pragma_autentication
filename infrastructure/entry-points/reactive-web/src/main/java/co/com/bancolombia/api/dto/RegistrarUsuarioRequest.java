package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Email;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistrarUsuarioRequest(
        @NotBlank String nombres,
        @NotBlank String apellidos,
        LocalDate fecha_nacimiento,
        String direccion,
        String telefono,
        @NotBlank @Email String correo_electronico,
        @NotNull @DecimalMin("0") @DecimalMax("15000000") BigDecimal salario_base
) {}
