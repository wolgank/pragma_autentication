package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.RegistrarUsuarioRequest;
import co.com.bancolombia.model.user.User;

public class UserMapper {
    public static User toDomain(RegistrarUsuarioRequest r) {
        return User.builder()
                .name(r.nombres())
                .lastname(r.apellidos())
                .birthdate(r.fecha_nacimiento())
                .address(r.direccion())
                .phoneNumber(r.telefono())
                .email(r.correo_electronico())
                .salaryBase(r.salario_base())
                .build();
    }
}
