package in.aishwarya.foodiesapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import in.aishwarya.foodiesapi.entity.FoodEntity;

public interface FoodRepository extends MongoRepository<FoodEntity, String> {

}
