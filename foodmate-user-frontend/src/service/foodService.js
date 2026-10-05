import axios from "axios";

const API_URL = `${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'}/foods`

export const fetchFoodList = async () => {
    try{
         const response = await axios.get(API_URL);
         return response.data;
    }catch (error){
        console.log('Error fetching food list : ',error);
        throw error;
    }
}

export const fetchFoodDetails = async(id) => {
    try{
        const response = await axios.get(API_URL + "/" + id);
        if(response.status === 200){
            return response.data;
        }
    }catch(error){
        console.log('Error fetching food details : ',error);
        throw error;
    }
}
