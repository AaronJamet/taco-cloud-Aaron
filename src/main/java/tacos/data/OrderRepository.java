package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.domain.TacoOrder;

public interface OrderRepository extends CrudRepository<TacoOrder ,String>{

	// Default methods already implemented by CrudRepository interface -> findAll(), findById(), save()...
}
