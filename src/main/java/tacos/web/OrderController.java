package tacos.web;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import tacos.data.OrderRepository;
import tacos.domain.TacoOrder;
import tacos.domain.Users;

@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
// Controller that handles HTTP GET requests for path '/orders/current'
public class OrderController {
	
	// Injection of OrderRepository for use it when saving an order to persist the data
	private OrderRepository orderRepo;
	
	public OrderController(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

	@GetMapping("/current")
	public String orderForm(@AuthenticationPrincipal Users user, @ModelAttribute TacoOrder order) {
		
		if (order.getDeliveryName() == null) {
			order.setDeliveryName(user.getFullname());
		}
		if (order.getDeliveryStreet() == null) {
			order.setDeliveryStreet(user.getStreet());
		}
		if (order.getDeliveryCity() == null) {
			order.setDeliveryCity(user.getCity());
		}
		if (order.getDeliveryState() == null) {
			order.setDeliveryState(user.getState());
		}
		if (order.getDeliveryZip() == null) {
			order.setDeliveryZip(user.getZip());
		}
		
		return "orderForm";
	}
	
	@PostMapping
	public String processOrder(@Valid TacoOrder order, Errors errors, 
			SessionStatus sessionStatus, @AuthenticationPrincipal Users user) {
		
		if (errors.hasErrors()) {
			// Devolver al formulario de pedidos/orders SI hay algún error
			return "orderForm";
		}
		
		// Links the order with the user, annotated as @AuthenticationPrincipal
		order.setUser(user);
		
		// Calls the orders repository for storing/save the data about this particular order
		orderRepo.save(order);
		sessionStatus.setComplete();
		
		return "redirect:/";
	}
}
