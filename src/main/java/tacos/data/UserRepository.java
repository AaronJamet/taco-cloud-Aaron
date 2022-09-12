package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.domain.Users;

public interface UserRepository extends CrudRepository<Users, Long> {
	
	Users findByUsername(String username);

}
