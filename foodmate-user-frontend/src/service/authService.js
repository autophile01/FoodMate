import axios from "axios";

const API_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

export const registerUser = async (data) => {
    try{
        const response = await axios.post(API_URL + "/register",data);
        return response;
    }catch (error){
        console.log('Error registering customer : ',error);
        throw error;
    }
}

export const loginUser = async (data) => {
    try{
        const response = await axios.post(API_URL + "/login",data);
        return response;
    }catch (error){
        console.log('Error logging customer in : ',error);
        throw error;
    }
}