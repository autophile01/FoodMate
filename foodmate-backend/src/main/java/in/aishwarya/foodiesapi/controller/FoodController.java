package in.aishwarya.foodiesapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.aishwarya.foodiesapi.io.FoodRequest;
import in.aishwarya.foodiesapi.io.FoodResponse;
import in.aishwarya.foodiesapi.service.FoodService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
@CrossOrigin("*")
public class FoodController {
	
	private final FoodService foodService;

	//Taking form-data and converting the json to object using ObjectMapper
	@PostMapping
	public FoodResponse addFood(@RequestPart("food") String foodString,
			                    @RequestPart("file") MultipartFile file) {
	
	ObjectMapper objectMapper = new ObjectMapper();
	FoodRequest request = null ;
	try {
		request = objectMapper.readValue(foodString,FoodRequest.class);
	}catch(JsonProcessingException ex) {
		 throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid JSON Format" );
	}
	
	//Passing the request object and adding to database
	FoodResponse response = foodService.addFood(request, file);
	return response;
	}
	
	
	//method to read all foods
	@GetMapping
	public List<FoodResponse> readFoods(){
		return foodService.readFoods();
	}
	
	//method to read one food with id
	@GetMapping("/{id}")
	public FoodResponse readFood(@PathVariable String id) {
		return foodService.readFood(id);
	}
	
	//method to delete food with id
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFood(@PathVariable String id){
		foodService.deleteFood(id);
	}
	
}
