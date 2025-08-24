package co.com.bancolombia.model.user;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

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
    private BigInteger id;
    private String name;
    private String lastname;
    private Date birthdate;
    private String address;
    private String phoneNumber;
    private String email;
    private BigDecimal salary;
}
