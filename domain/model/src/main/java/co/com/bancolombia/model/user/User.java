package co.com.bancolombia.model.user;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String name;
    private String lastname;
    private LocalDate birthdate;
    private String address;
    private String phoneNumber;
    private String email;
    private BigDecimal salaryBase;
}
