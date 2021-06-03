package pt.santander.clientesrestapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.santander.clientesrestapi.entity.Customer;

import java.util.List;

/**
 * The repository provides access to a Database.
 * Keywords can be combined to create simple queries
 */
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    List<Customer> findByActiveAndNameContaining(Boolean active, String name);
    List<Customer> findByActiveAndNif(Boolean active, String nif);
    List<Customer> findByActiveAndNifAndNameContaining(Boolean Active, String nif, String name);
    List<Customer> findByActive(Boolean Active);

}
