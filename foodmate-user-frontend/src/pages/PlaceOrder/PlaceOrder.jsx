import React, { useContext, useState } from "react";
import "./PlaceOrder.css";
import {assets} from '../../assets/assets';
import { StoreContext } from "../../context/StoreContext";
import { calculateCartTotals } from "../../util/cartUtils";
import {RAZORPAY_KEY} from "../../util/constants";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import axios from "axios";

const PlaceOrder = () => {

  const {foodList, quantities, setQuantities, token} = useContext(StoreContext);
  const navigate = useNavigate();

  const [data, setData] = useState({
    firstName: '',
    lastName: '',
    email:'',
    phoneNumber:'',
    address:'',
    state:'',
    city:'',
    zip:''
  });

  const onChangeHandler = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setData(data => ({...data, [name]:value}));
  }

  const onSubmitHandler = async (event) => {
    event.preventDefault();
    console.log("data", data);
        const orderData = {
        userAddress: `${data.firstName} ${data.lastName}, ${data.address}, ${data.city}, ${data.state}, ${data.zip}`,
        phoneNumber: data.phoneNumber,
        email: data.email,
        orderedItems: cartItems.map(item => ({
            foodId: item.foodId,
            quantity: quantities[item.id],
            price: item.price * quantities[item.id],
            category: item.category,
            imageUrl: item.imageUrl,
            description: item.description,
            name: item.name
        })),
        amount: total.toFixed(2),
        orderStatus: "Preparing"
    };
    console.log(orderData);
    try{
      console.log("Before API call");
      console.log("Token:", token);
       const response = await axios.post(`${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'}/orders/create`, orderData, {headers : {'Authorization' : `Bearer ${token}`} });
       console.log("After API call");
       console.log(response.status);
       if(response.status === 201 && response.data.razorpayOrderId){
         //initiate the payment
         initiateRazorpayPayment(response.data);
       }else{
        console.log(error.message);
        toast.error("Unable to place order. please try again");
       }
    }catch(error){
        console.log(error);
        console.log(error.message);
        toast.error("Unable to place order. please try again");
    }
  }

  const initiateRazorpayPayment = (order) => {
     const options = {
        key: RAZORPAY_KEY,
        amount: order.amount,
        currency: "INR",
        name: "Food Land",
        description: "Food Order Payment",
        order_id: order.razorpayOrderId,
        handler: async function(razorResponse){
           await verifyPayment(razorResponse)
        },
        prefill:{
          name: `${data.firstName} ${data.lastName}`,
          email: data.email,
          contact: data.phoneNumber
        },
        theme: {color:"#3399cc"},
        modal: {
        ondismiss: async function () {
        toast.error("Payment cancelled.");
        await deleteOrder(order.id);
        console.log("Deleted");
         }
       }
     };

     const razorpay = new window.Razorpay(options);
     razorpay.open();
     console.log("Successss");
  }

  const verifyPayment = async (razorpayResponse) => {
     const paymentData = {
      razorpay_payment_id : razorpayResponse.razorpay_payment_id,
      razorpay_order_id : razorpayResponse.razorpay_order_id,
      razorpay_signature : razorpayResponse.razorpay_signature
     };
      try{
      const response = await axios.post(`${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'}/orders/verify`, paymentData, {headers : {'Authorization' : `Bearer ${token}`} });
      if(response.status === 200){
        toast.success("Payment Successfull");
        console.log("Payment successfull");
        await clearCart();
        navigate("/myorders");
      }else{
         toast.error("Payment Failed, Please try again");
         navigate("/");
      }}
      catch(error){
        toast.error("Payment Failed, Please try again");
      }
  }

  const deleteOrder = async (orderId) => {
     try{
        await axios.delete(`${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'}/orders/${orderId}`, {headers : {'Authorization' : `Bearer ${token}`} });
     }catch(error){
        toast.error("Something went wrong. Contact support");
     }
  }

  const clearCart = async () => {
    try{
       console.log("Calling clear cart...");
       await axios.delete(`${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'}/cart`, {headers : {'Authorization' : `Bearer ${token}`} });
       setQuantities({});
       console.log("Complete clear cart...");
    }catch(error){
      console.log("Calling clear cart error...");
      console.log(error.message);
      toast.error("Error while clearing the cart");
    }
  }

   // Cart Items
  const cartItems = foodList.filter((food) => quantities[food.id] > 0);

  const {subtotal, shipping, tax, total} = calculateCartTotals(cartItems, quantities);

  return (
    <div className="container py-3">
      <div className="py-4 text-center">
      <img className="d-block mx-auto mb-2" src={assets.logo} alt="" width="88" height="88" />
      </div>
      <div className="row justify-content-center">
        <div className="col-lg-10 col-xl-9">

          <div className="row g-5">
            <div className="col-md-5 col-lg-4 order-md-last">
              <h4 className="d-flex justify-content-between align-items-center mb-3">
                <span className="text-primary">Your Cart</span>
                <span className="badge bg-primary rounded-pill">{cartItems.length}</span>
              </h4>
              <ul className="list-group mb-3">
                {cartItems.map(item => 
                  (<li className="list-group-item d-flex justify-content-between lh-sm">
                  <div>
                    <h6 className="my-0">{item.name}</h6>
                    <small className="text-muted">Qty: {quantities[item.id]}</small>
                  </div>
                  <span className="text-muted">₹{item.price * quantities[item.id]}</span>
                </li>)
                )}
                <li className="list-group-item d-flex justify-content-between">
                  <div>
                    <span className="text-muted">Shipping</span>
                  </div>
                  <span className="text-muted">₹{subtotal === 0 ? 0.0 : shipping.toFixed(2)}</span>
                </li>
                <li className="list-group-item d-flex justify-content-between">
                  <div>
                    <span className="text-muted">Tax(10%)</span>
                  </div>
                  <span className="text-muted">₹{tax.toFixed(2)}</span>
                </li>
                <li className="list-group-item d-flex justify-content-between">
                  <span>Total (INR)</span>
                  <strong>₹{total.toFixed(2)}</strong>
                </li>
              </ul>           
            </div>

            {/* Billing Form */}
            <div className="col-md-7 col-lg-8">
              <h4 className="mb-3">Billing Address</h4>
              <form className="needs-validation" onSubmit={onSubmitHandler}>
                <div className="row g-3">
                  <div className="col-sm-6">
                    <label htmlFor="firstName" className="form-label">
                      First Name
                    </label>
                    <input
                      type="text"
                      id="firstName"
                      className="form-control"
                      placeholder="Aishwarya"
                      required
                      onChange={onChangeHandler}
                      name="firstName"
                      value={data.firstName}
                    />
                  </div>

                  <div className="col-sm-6">
                    <label htmlFor="lastName" className="form-label">
                      Last Name
                    </label>
                    <input
                      type="text"
                      id="lastName"
                      className="form-control"
                      placeholder="Motghare"
                      required
                      onChange={onChangeHandler}
                      name="lastName"
                      value={data.lastName}
                    />
                  </div>

                  <div className="col-12">
                    <label htmlFor="username" className="form-label">
                      Email
                    </label>
                    <div className="input-group">
                      <span className="input-group-text">@</span>
                      <input
                        type="email"
                        id="email"
                        className="form-control"
                        placeholder="Email"
                        required
                        onChange={onChangeHandler}
                        name="email"
                        value={data.email}
                      />
                    </div>
                  </div> 

                  <div className="col-12">
                    <label htmlFor="address" className="form-label">
                      Phone Number
                    </label>

                    <input
                      type="number"
                      id="phone"
                      className="form-control"
                      placeholder="9876567863"
                      required
                      onChange={onChangeHandler}
                      name="phoneNumber"
                      value={data.phoneNumber}
                    />
                  </div>                 

                  <div className="col-12">
                    <label htmlFor="address" className="form-label">
                      Address
                    </label>

                    <input
                      type="text"
                      id="address"
                      className="form-control"
                      placeholder="1234 Main St"
                      required
                      onChange={onChangeHandler}
                      name="address"
                      value={data.address}
                    />
                  </div>

                  <div className="col-md-5">
                    <label htmlFor="state" className="form-label">
                      State
                    </label>

                    <select id="state" className="form-select" onChange={onChangeHandler}
                      name="state"
                      value={data.state}>
                      <option>Choose...</option>
                      <option>Maharashtra</option>
                    </select>
                  </div>

                  <div className="col-md-4">
                    <label htmlFor="city" className="form-label">
                      City
                    </label>

                    <select id="city" className="form-select" onChange={onChangeHandler}
                      name="city"
                      value={data.city}>
                      <option>Choose...</option>
                      <option>Mumbai</option>
                    </select>
                  </div>

                  <div className="col-md-3">
                    <label htmlFor="zip" className="form-label">
                      Zip
                    </label>

                    <input
                      type="number"
                      id="600512"
                      className="form-control"
                      required
                      onChange={onChangeHandler}
                      name="zip"
                      value={data.zip}
                    />
                  </div>

                </div>

                <hr className="my-4" />

                <button
                  type="submit"
                  className="w-100 btn btn-primary btn-lg"
                  disabled={cartItems.length === 0}
                >
                  Continue to Checkout
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PlaceOrder;