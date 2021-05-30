package pt.santander.clientesrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.santander.clientesrestapi.dto.CustomerResponse;
import pt.santander.clientesrestapi.service.CustomerService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * The controller is the entrypoint. In order words is the interface where systems send/receive data.
 */
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerServ;

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponse>> getCustomer(
            @RequestParam(required=false) String name,
            @RequestParam(required=false) String nif) throws Exception {
        List<CustomerResponse> list = customerServ.getCustomers(name, nif);
        if (!list.isEmpty()) {
            return ResponseEntity.status(200).body(list);
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}
