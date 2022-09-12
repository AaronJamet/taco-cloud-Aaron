package tacos.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;
import tacos.domain.Ingredient;
import tacos.domain.Ingredient.Type;
import tacos.domain.Taco;
import tacos.domain.TacoOrder;
import tacos.domain.Users;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {
	
	// Repository´s interface that will be injected into this class
	private final IngredientRepository ingredientRepo;
	private TacoRepository tacoRepo;
	private UserRepository userRepo;
	
	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository tacoRepo,
			UserRepository userRepo) {
		
		this.ingredientRepo = ingredientRepo;
		this.tacoRepo = tacoRepo;
		this.userRepo = userRepo;
	}
	
	@ModelAttribute
	public void addIngredientsToModel(Model model) {
		
		// Fetch all ingredients in the database, and filters them by types
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepo.findAll().forEach(i -> ingredients.add(i));
		
		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
			// Añadir los valores de cada tipo enumerado como atributos al Modelo
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
	}
	
	@ModelAttribute(name = "order")
	public TacoOrder order() {
		return new TacoOrder();
	}
	
	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}
	
	@ModelAttribute(name = "user")
	public Users user (Principal principal) {
		
		String username = principal.getName();
		Users user = userRepo.findByUsername(username);
		
		return user;
	}
	
	@GetMapping
	public String showDesignForm() {
		return "design";
	}
	@PostMapping
	// Recieves fields in the form bound to properties of a Taco object
	// @ModelAttribute in TacoOrder parameter indicates tha it should use object placed into the model 
	// in method 'order()' above -> @ModelAttribute(name = "tacoOrder")
	public String processTaco(@Valid Taco taco, Errors errors,
			@ModelAttribute TacoOrder tacoOrder) {
		
		log.info("---- Saving taco");
		
		if (errors.hasErrors()) {
			return "design";
		}
		
		Taco saved = tacoRepo.save(taco);
		order().addTaco(saved);
		log.info("Processing taco: {}", taco);
		
		return "redirect:/orders/current";
	}

	private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients
				.stream()
				.filter(x -> x.getType().equals(type))
				.collect(Collectors.toList());
	}
}
