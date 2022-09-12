package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.domain.Ingredient;

// Interface needs to extend from Spring Data´s CrudRepository class, to create the implementation
// of the repository automatically, and the default methods -> findAll(), findById(), and save()
// 1º parameter is type of object to be persisted, and 2º is that object´s Id type
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
	
	// Default methods already implemented by CrudRepository interface -> findAll(), findById(), save()...
}
