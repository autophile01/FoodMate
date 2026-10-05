package in.aishwarya.foodiesapi.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.razorpay.RazorpayException;

import in.aishwarya.foodiesapi.io.OrderRequest;
import in.aishwarya.foodiesapi.io.OrderResponse;

public interface OrderService {

	OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException;
	
	void verifyPayement(Map<String,String> paymentData, String status);
	
    List<OrderResponse> getUserOrders();
    
    void removeOrder(String orderId);
    
    //for admin
    List<OrderResponse> getOrdersOfAllUsers();
    void updateOrderStatus(String orderId, String status);
    
}
