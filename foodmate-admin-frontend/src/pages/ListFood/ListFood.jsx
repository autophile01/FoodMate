import React from 'react'
import { useState, useEffect } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import './ListFood.css';
import { getFoodList, deleteFood } from '../../services/FoodService';

//Function for listing foods
const ListFood = () => {
  const [list,setList] = useState([]);
  const fetchList = async() => {
    try{
       const data = await getFoodList();
       setList(data);
    }catch(error){
       toast.error('Error while reading the foods.');
    }
  }

  //Function for removing foods
  const removeFood = async (foodId) => {
    try{
      const success = await deleteFood(foodId);
      if(success){
        await fetchList();
        toast.success('Food removed.');
      }else{
        toast.error('Error while removing the food.');
      }
    }catch(error){
       toast.error('Error while removing the food.');
    }
  }

  useEffect(() => {
    fetchList();
  },[]);

  return (
    <div className="py-5 row justify-content-center">
      <div className="col-11 card">
        <table className='table'>
          <thead>
            <tr>
              <th>Image</th>
              <th>Name</th>
              <th>Category</th>
              <th>Price</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {
              list.map((item,index) => {
                return(
                  <tr>
                    <td>
                      <img src={item.imageUrl} alt='' height={48} width={48}></img>
                    </td>
                    <td>
                      {item.name}
                    </td>
                    <td>
                      {item.category}
                    </td>
                    <td>
                      ₹{item.price}.00
                    </td>
                    <td className='text-danger'>
                      <i className='bi bi-x-circle-fill' onClick={() => removeFood(item.id)}></i>
                    </td>
                  </tr>
                )
              })
            }
          </tbody>
        </table>
      </div>
      
    </div>
  )
}

export default ListFood;
