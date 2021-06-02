package pt.santander.clientesrestapi.service;

import pt.santander.clientesrestapi.dto.CustomerRequest;
import pt.santander.clientesrestapi.dto.CustomerResponse;

import java.util.List;

/**
 * The interface define the contract of the business logic
 * The implementation may vary regarding the business logic, e.g., retrieve from database, stream, file, etc.
 */
public interface CustomerService {
       public List<CustomerResponse> getCustomers(String name, String nif) throws Exception;
       public CustomerResponse createCustomer(CustomerRequest request) throws Exception;
       public CustomerResponse updateCustomer(Integer id, CustomerRequest request) throws Exception;
       public void deleteCustomer(Integer id) throws Exception;
       public List<CustomerResponse> getCustomersByEmailGroup(String group) throws Exception;
}
