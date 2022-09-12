package tacos.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import tacos.data.IngredientRepository;
import tacos.domain.Ingredient;

@Component
// Converts String parameters in the form into Ingredient objects, using the Id
public class IngredientByIdConverter implements Converter<String, Ingredient> {

	private IngredientRepository ingredientRepo;
	
	@Autowired
	public IngredientByIdConverter(IngredientRepository ingredientRepo) {
		// Inyección del objeto a través de parámetros del constructor
		this.ingredientRepo = ingredientRepo;		
	}
	
	@Override
	public Ingredient convert(String id) {
		
		return ingredientRepo.findById(id).orElse(null);
	}
	
}
