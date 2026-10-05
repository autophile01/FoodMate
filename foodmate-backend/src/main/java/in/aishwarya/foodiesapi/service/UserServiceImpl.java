package in.aishwarya.foodiesapi.service;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import in.aishwarya.foodiesapi.entity.UserEntity;
import in.aishwarya.foodiesapi.io.UserRequest;
import in.aishwarya.foodiesapi.io.UserResponse;
import in.aishwarya.foodiesapi.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationFacade authenticationFacade;

	@Override
	public UserResponse registerUser(UserRequest request) {
		UserEntity newUser = convertToEntity(request);
		newUser = userRepository.save(newUser);
		return convertToResponse(newUser);
	}
	
	private UserEntity convertToEntity(UserRequest request) {
		return UserEntity.builder()
		          .email(request.getEmail())
		  	      .name(request.getName())
		  	      .password(passwordEncoder.encode(request.getPassword()))
		          .build();
	}
	
	private UserResponse convertToResponse(UserEntity registeredUser) {
		return UserResponse.builder()
		            .id(registeredUser.getId())
		            .name(registeredUser.getName())
		            .email(registeredUser.getEmail())
		            .build();	
	}

	@Override
	public String findByUserId() {
	    Authentication auth = authenticationFacade.getAuthentication();
	    String loggedInUserEmail = auth.getName();
	    UserEntity loggedinUser = userRepository.findByEmail(loggedInUserEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	    return loggedinUser.getId();
	}

}
