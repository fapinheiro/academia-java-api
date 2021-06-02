package pt.santander.clientesrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String nif;

    @Email
    private String email;

}
