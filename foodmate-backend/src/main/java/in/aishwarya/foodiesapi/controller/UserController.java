package in.aishwarya.foodiesapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import in.aishwarya.foodiesapi.io.UserRequest;
import in.aishwarya.foodiesapi.io.UserResponse;
import in.aishwarya.foodiesapi.service.UserService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {
	
	private final UserService userService;

	 @PostMapping("/register")
	 @ResponseStatus(HttpStatus.CREATED)
	 public UserResponse register(@RequestBody UserRequest request) {
		 return userService.registerUser(request);
	 }
}
