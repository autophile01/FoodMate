package in.aishwarya.foodiesapi.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import com.razorpay.RazorpayException;

import in.aishwarya.foodiesapi.io.OrderRequest;
import in.aishwarya.foodiesapi.io.OrderResponse;
import in.aishwarya.foodiesapi.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody OrderRequest request) throws RazorpayException {

        return orderService.createOrderWithPayment(request);
    }
    
    @PostMapping("/verify")
    public void verifyPayment(@RequestBody Map<String, String> paymentData) {
    	orderService.verifyPayement(paymentData, "Paid");
    }
    
    @GetMapping
    public List<OrderResponse> getOrders() {
    	 return orderService.getUserOrders();
    }
    
    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String orderId) {
    	orderService.removeOrder(orderId);
    }
    
    //for-admin
    
    @GetMapping("/all")
    public List<OrderResponse> getOrdersOfAllUsers(){
    	return orderService.getOrdersOfAllUsers();
    }
    
    @PatchMapping("/status/{orderId}")
    public void updateOrderStatus(@PathVariable String orderId,@RequestParam String status) {
    	orderService.updateOrderStatus(orderId, status);
    }
}
