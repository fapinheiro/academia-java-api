package pt.santander.clientesrestapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.santander.clientesrestapi.dto.CustomerResponse;
import pt.santander.clientesrestapi.repository.CustomerRepository;
import pt.santander.clientesrestapi.service.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The business logic to retrieve a list of customers from the database.
 */
@Service
public class CustomerServiceDataBaseImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRep;

    @Override
    public List<CustomerResponse> getCustomers(String name, String nif) throws Exception {
        List<CustomerResponse> customers = new ArrayList<>();
        if (name != null && nif != null) {

            // Search by name and nif, apply conversion, normalize and add to the list
            customers.addAll(customerRep.findByActiveAndNifAndNameContaining(true, nif, name)
                    .stream()
                    .map( customer -> {
                        return CustomerResponse.builder()
                                .id(customer.getId())
                                .name(customer.getName())
                                .nif(customer.getNif())
                                .email(customer.getEmail())
                                .active(customer.getActive())
                                .build();
                    })
                    .collect(Collectors.toList())
            );


        } else if (name != null) {
            // Search by name, apply conversion, normalize and add to the list
            customers.addAll(customerRep.findByActiveAndNameContaining(true, name)
                    .stream()
                    .map( customer -> {
                        return CustomerResponse.builder()
                                .id(customer.getId())
                                .name(customer.getName())
                                .nif(customer.getNif())
                                .email(customer.getEmail())
                                .active(customer.getActive())
                                .build();
                    })
                    .collect(Collectors.toList())
            );
        } else if (nif != null) {
            // Search by nif, apply conversion, normalize and add to the list
            customers.addAll(customerRep.findByActiveAndNif( true, nif)
                    .stream()
                    .map( customer -> {
                        return CustomerResponse.builder()
                                .id(customer.getId())
                                .name(customer.getName())
                                .nif(customer.getNif())
                                .email(customer.getEmail())
                                .active(customer.getActive())
                                .build();
                    })
                    .collect(Collectors.toList())
            );
        } else {
            // Search by active, apply conversion, normalize and add to the list
            customers.addAll(customerRep.findByActive(true)
                    .stream()
                    .map( customer -> {
                        return CustomerResponse.builder()
                                .id(customer.getId())
                                .name(customer.getName())
                                .nif(customer.getNif())
                                .email(customer.getEmail())
                                .active(customer.getActive())
                                .build();
                    })
                    .collect(Collectors.toList())
            );
        }
        return customers;
    }
}
