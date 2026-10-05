package in.aishwarya.foodiesapi.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.aishwarya.foodiesapi.io.AuthenticationRequest;
import in.aishwarya.foodiesapi.io.AuthenticationResponse;
import in.aishwarya.foodiesapi.service.AppUserDetailsService;
import in.aishwarya.foodiesapi.util.JwtUtil;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	private final AppUserDetailsService userDetailsService;
	private final JwtUtil jwtUtil;

	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		final String jwtToken = jwtUtil.generateToken(userDetails);
		return new AuthenticationResponse(request.getEmail(), jwtToken);
	}
	
	
}
