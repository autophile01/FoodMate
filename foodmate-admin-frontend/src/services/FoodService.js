import axios from "axios";

const API_URL = `${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'}/foods`;

//API backend call for adding food 
export const addFood = async (foodData, image) => {
    const formData = new FormData();
    formData.append('food', JSON.stringify(foodData));
    formData.append('file', image);
    try{
        await axios.post(API_URL, formData, {headers: {"Content-Type" : "multipart/form-data"}});
    }catch (error){
         console.log('Error',error);
         throw error;
    }
}

//API backend call for displaying food
export const getFoodList = async () => {
    try{
        const response = await axios.get(API_URL);
        return response.data;
    }catch (error){
         console.log('Error fetching food List',error);
         throw error;
    }
}

//API backend call for deleteing food
export const deleteFood = async (foodId) => {
    try{
        const response = await axios.delete(API_URL +"/"+foodId);
        return response.status === 204;
    }catch(error){
        console.log('Error while removing the food.');
        throw error;
    }
} 