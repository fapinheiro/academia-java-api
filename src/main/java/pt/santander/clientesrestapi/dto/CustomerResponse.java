package pt.santander.clientesrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomerResponse {

    private Integer id;
    private String name;
    private String nif;
    private String email;
    private Boolean active;

}
