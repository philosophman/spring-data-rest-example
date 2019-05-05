package bankaccount.dao;

import bankaccount.domain.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CustomerDao extends PagingAndSortingRepository<Customer, Long> {

}
