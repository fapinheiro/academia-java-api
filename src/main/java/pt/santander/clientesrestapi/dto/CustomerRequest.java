package pt.santander.clientesrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    @Size(min = 0,max = 9)
    private String nif;

    @NotEmpty
    @Email
    private String email;

}
