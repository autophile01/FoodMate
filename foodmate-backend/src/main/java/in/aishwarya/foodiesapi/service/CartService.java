package in.aishwarya.foodiesapi.service;

import org.springframework.stereotype.Service;

import in.aishwarya.foodiesapi.io.CartRequest;
import in.aishwarya.foodiesapi.io.CartResponse;

public interface CartService {
  CartResponse addToCart(CartRequest request);
  
  CartResponse getCart();
  
  void clearCart();
  
  CartResponse removeFromCart(CartRequest cartRequest);
}
