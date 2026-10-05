package in.aishwarya.foodiesapi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import in.aishwarya.foodiesapi.entity.FoodEntity;
import in.aishwarya.foodiesapi.io.FoodRequest;
import in.aishwarya.foodiesapi.io.FoodResponse;
import in.aishwarya.foodiesapi.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

//import lombok.AllArgsConstructor;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
	
	private final FoodRepository foodRepository;
	
	@Value("${aws.s3.bucketname}")
	private String bucketName;
	
	@Override
	public String uploadFile(MultipartFile file) {
	    try {
	        String fileName =
	                UUID.randomUUID() + "_" + file.getOriginalFilename();
	        Path uploadPath = Paths.get("uploads");
	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }
	        Files.copy(
	                file.getInputStream(),
	                uploadPath.resolve(fileName),
	                StandardCopyOption.REPLACE_EXISTING
	        );
	        return "http://localhost:8080/uploads/" + fileName;
	    } catch (IOException e) {
	        throw new ResponseStatusException(
	                HttpStatus.INTERNAL_SERVER_ERROR,
	                "File upload failed"
	        );
	    }
	}
	
	@Override
	public FoodResponse addFood(FoodRequest request, MultipartFile file) {
		FoodEntity newFoodEntity = convertToEntity(request);
		String imageUrl = uploadFile(file);
		newFoodEntity.setImageUrl(imageUrl);
		newFoodEntity = foodRepository.save(newFoodEntity);
		return convertToResponse(newFoodEntity);
	}
	
	@Override
	public List<FoodResponse> readFoods(){
		List<FoodEntity> databaseEntries = foodRepository.findAll();
		return databaseEntries.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());
	}
	
	@Override
	public FoodResponse readFood(String id) {
		FoodEntity existingFood = foodRepository.findById(id).orElseThrow(() -> new RuntimeException("Food not found for the id:" + id));
		return convertToResponse(existingFood);
	}
	
	private FoodEntity convertToEntity(FoodRequest request) {
		return FoodEntity.builder()
		          .name(request.getName())
		          .description(request.getDescription())
		          .category(request.getCategory())
		          .price(request.getPrice())
		          .build();		
	}
	
	private FoodResponse convertToResponse(FoodEntity entity) {
		return FoodResponse.builder()
		            .id(entity.getId())
		            .name(entity.getName())
		            .description(entity.getDescription())
		            .category(entity.getCategory())
		            .price(entity.getPrice())
		            .imageUrl(entity.getImageUrl())
		            .build();
	}
	
//DELETING FILE FROM UPLOADS
	//1. REQUIRES FILE NAME
	@Override
	public void deleteFood(String id) {
		FoodResponse response = readFood(id);
		String imageUrl = response.getImageUrl();
		String filename = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
		boolean isFileDeleted = deleteFile(filename);	
		System.out.println("OUTPUT : " + isFileDeleted);
		if(isFileDeleted) {
			//if file deleted from local storage, delete data from db as well
			foodRepository.deleteById(response.getId());
		}
	}
	
	//2. FILE NAME GOT, NOW DELETE(FROM LOCAL STORGAE)
	@Override
	public boolean deleteFile(String filename) {
	    try {
	        Path filePath = Paths.get("uploads", filename);
	        return Files.deleteIfExists(filePath);
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to delete file", e);
	    }
	}
    
	//DELETING FILE FOR AWS
//	@Override
//	public boolean deleteFile(String filename) {
//		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
//				.bucket(bucketName)
//				.key(filename)
//				.build();
//	    s3Client.deleteObject(deleteObjectRequest);
//		return false;
//	}

	
//private final S3Client s3Client = null;
//	@Override
//	public String uploadFile(MultipartFile file) {
//	    String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
//		String key = UUID.randomUUID().toString()+"."+filenameExtension;
//		try {
//			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//					.bucket(bucketName)
//					.key(key)
//					.acl("public-read")
//					.contentType(file.getContentType())
//					.build();
//			
//			PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
//			if(response.sdkHttpResponse().isSuccessful()) {
//				return "https://" + bucketName + ".s3.amazonaws.com/" + key;
//			}else {
//				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed");
//			}
//		}catch(IOException ex){
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occured while uploading the file");
//		}
//	}
//
       
}
