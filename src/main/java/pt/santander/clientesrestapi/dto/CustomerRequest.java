package pt.santander.clientesrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String nif;

    @NotNull
    @Email
    private String email;

}
