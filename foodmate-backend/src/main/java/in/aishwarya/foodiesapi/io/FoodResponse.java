package in.aishwarya.foodiesapi.io;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodResponse {
	
	@Id
	private String id;
	private String name;
    private String description;
    private String imageUrl;
    private double price;
    private String category;
}
