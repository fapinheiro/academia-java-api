package pt.santander.clientesrestapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.santander.clientesrestapi.dto.CustomerRequest;
import pt.santander.clientesrestapi.dto.CustomerResponse;
import pt.santander.clientesrestapi.entity.Customer;
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
        } else if (nif == null) {
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
        }
        return customers;
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) throws Exception {
        // Convert dto to entity and save
        Customer customerNew = customerRep.save(
                Customer.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .nif(request.getNif())
                        .active(true)
                        .build()
        );
        // Convert entity to dto and return
        return CustomerResponse.builder()
                .id(customerNew.getId())
                .name(customerNew.getName())
                .nif(customerNew.getNif())
                .email(customerNew.getEmail())
                .active(customerNew.getActive())
                .build();
    }

    @Override
    public CustomerResponse updateCustomer(Integer id, CustomerRequest request) throws Exception {
        Customer customerUpdated = customerRep.findById(id)
                .map( customer -> {
                    customer.setEmail(request.getEmail());
                    customer.setName(request.getName());
                    customer.setNif(request.getNif());
                    return customerRep.save(customer);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER NOT FOUND"));
        return CustomerResponse.builder()
                .id(customerUpdated.getId())
                .name(customerUpdated.getName())
                .nif(customerUpdated.getNif())
                .email(customerUpdated.getEmail())
                .active(customerUpdated.getActive())
                .build();
    }

    @Override
    public void deleteCustomer(Integer id) throws Exception {
        customerRep.findById(id)
                .map( customer -> {
                    customer.setActive(false);
                    return customerRep.save(customer);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER NOT FOUND"));
    }

}
