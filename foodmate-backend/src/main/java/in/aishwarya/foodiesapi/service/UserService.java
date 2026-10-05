package in.aishwarya.foodiesapi.service;

import in.aishwarya.foodiesapi.io.UserRequest;
import in.aishwarya.foodiesapi.io.UserResponse;

public interface UserService {

	UserResponse registerUser(UserRequest request);
	
	String findByUserId();
}
